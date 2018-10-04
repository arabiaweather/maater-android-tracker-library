package tracker.android.tracker_android_poc

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import io.socket.client.IO

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject
import java.util.logging.Logger


const val USER_ID = "USER_ID"
const val CLIENT_ID = "CLIENT_ID"
const val NAME = "NAME"
const val EMAIL = "EMAIL"

const val TRACKING_URL = BuildConfig.TRACKING_URL


const val BROADCAST_STOP_TRACKING = "BROADCAST_STOP_TRACKING"

private var trackingId = 0

class TrackingService : Service() {

    val TAG = "TRACKING"

    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                BROADCAST_STOP_TRACKING -> disconnect()
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? {

        return null
    }



    private var userId = 0
    private var clientId = 0
    private var name = ""
    private var email = ""


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LocalBroadcastManager.getInstance(this).registerReceiver(broadCastReceiver, IntentFilter(BROADCAST_STOP_TRACKING))
        Log.i(TAG, "TrackingService onStartCommand, $trackingId")
        userId = intent?.extras?.getInt(USER_ID) ?: 0
        clientId = intent?.extras?.getInt(CLIENT_ID) ?: 0
        name = intent?.extras?.getString(NAME) ?: ""
        email = intent?.extras?.getString(EMAIL) ?: ""

        connect(userId, clientId, name, email)
        return Service.START_NOT_STICKY
    }

    data class TrackingCommand(
            val userId: Int,
            val clientId: Int,
            val name: String,
            val email: String
    )

    var socket = IO.socket("http://localhost:8282")

    fun connect(userId: Int, clientId: Int, name: String, email: String) {

        if (socket.connected()) {
            Log.i(TAG,"socket allready connected, SKIPPING 1")
            stopSelf()
            return
        }

        if (trackingId != 0){
            Log.i(TAG,"tracking id already set ${trackingId} , SKIPPING 2")
            stopSelf()
            return
        }

        val json = JSONObject()
        json.put("user_id",userId)
        json.put("client_id",clientId)
        json.put("name",name)
        json.put("email",email)
        json.put("command","track")
        json.put("os","Android")
        val v = android.os.Build.VERSION.SDK_INT;
        json.put("os_version","$v")
        json.put("client_type","mobile")
        json.put("device_brand",android.os.Build.BRAND)
        json.put("device_model",android.os.Build.MODEL)
        val android = 2
        json.put("device_type",android)
        val maater_app_id = 2
        json.put("app_id",maater_app_id)


        val opts = IO.Options()
        opts.secure = TRACKING_URL.startsWith("https://")
        opts.forceNew = true
        opts.reconnection = true

        socket = IO.socket(TRACKING_URL,opts)

        socket.on(Socket.EVENT_CONNECT) {
            Log.i(TAG,"socket connected")
            emitTrackDelayed(json.toString())
        }

        socket.on(Socket.EVENT_ERROR) {errs ->
            val err = errs[0] as Throwable
            Log.e(TAG,"socket error:",err)
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.i(TAG,"socket disconnect")
        }

        socket.on("tracked", onTracked)

        Log.i(TAG, "socket connect to: $TRACKING_URL ...")
        socket.connect()
    }

    fun disconnect(){
        Log.i(TAG,"disconnect()")
        trackingId = 0
        socket.disconnect()
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(broadCastReceiver)
    }

    // this has to be delayed, because of a bug in Android and Ahmed Awad
    private fun emitTrackDelayed(json: String){
        java.util.Timer().schedule(
                object : java.util.TimerTask() {
                    override fun run() {
                        socket.emit("track", json)
                    }
                },3000
        )
    }

    private val onTracked = Emitter.Listener { args ->
        val json = args[0] as JSONObject
        val tid = json.getInt("id")
        Log.i(TAG,"tracking SUCCESS $tid")
        trackingId = tid
    }

}
