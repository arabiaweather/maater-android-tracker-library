package tracker.android.tracker_android_poc

import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.socket.client.IO

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


class TrackingService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
//        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_NOT_STICKY
    }

    var socket = IO.socket("http://macbook-air.duckdns.org:8282")

    fun connect() {

        val opts = IO.Options()
        opts.secure = true
        opts.forceNew = true
        opts.reconnection = true

        socket.on(Socket.EVENT_CONNECT, Emitter.Listener {
            println("connected")
            socket.emit("foo", "hi")
            socket.emit("asdf","324")
        })
        socket.connect()
    }
}
