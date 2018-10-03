package tracker.android.tracker_android_poc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.socket.client.IO

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject

val USER_ID = "USER_ID"
val CLIENT_ID = "CLIENT_ID"
val NAME = "NAME"
val EMAIL = "EMAIL"
val TRACKING_ID = "TRACKING_ID"
val APP_ID = "APP_ID"


class TrackingService : Service() {


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
//        return null
    }

    val trackingId = ""

    private var userId = 0
    private var clientId = 0
    private var name = ""
    private var email = ""

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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

    var socket = IO.socket("http://macbook-air.duckdns.org:8282")

    fun connect(userId: Int, clientId: Int, name: String, email: String) {

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
        json.put("app_id",2)


        val opts = IO.Options()
        opts.secure = true
        opts.forceNew = true
        opts.reconnection = true

        socket.on(Socket.EVENT_CONNECT, Emitter.Listener {
            println("socket connected")
            this.socket.emit("track", json.toString())
        }).on(Socket.EVENT_ERROR, Emitter.Listener {
            println("error:")
            println(it)
        })

        socket.on("tracked", Emitter.Listener {
            println("tracked")
            println(it)
        })
        socket.connect()
    }
}
