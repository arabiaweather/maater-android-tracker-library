package com.example.maaterandroidtracker

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

class Tracker
private constructor(private val applicationContext: Application) {

    val TAG = Tracker::class.java.simpleName

    private var userId = 0
    private var clientId = 0
    private var name = ""
    private var email = ""

    fun resumeTrackingUser(userId: Int = 0, clientId: Int = 0, name: String = "", email: String = "") {
        this.userId = userId
        this.clientId = clientId
        this.name = name
        this.email = email
    }

    fun trackUser(userId: Int = 0, clientId: Int = 0, name: String = "", email: String = "") {
        Log.i(TAG, "trackUser(userId: $userId, clientId: $clientId, name: String = \"$name\", email: String = \"$email\")")
        if (userId == 0){
            Log.i(TAG, "Tracker trackUser userId is zero, SKIPPING 0")
            return
        }
        this.userId = userId
        this.clientId = clientId
        this.name = name
        this.email = email

        val i = Intent(applicationContext, TrackingService::class.java)
        i.apply {
            this.putExtra(USER_ID, userId)
            this.putExtra(CLIENT_ID, clientId)
            this.putExtra(NAME, name)
            this.putExtra(EMAIL, email)
        }
        applicationContext.startService(i)
    }

init {
    applicationContext.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

        private var numStarted: Int = 0
            set(value) {
                if (value <= 0) {
                    Log.i(TAG, "value <= 0")
                    val intent = Intent()
                    intent.action = BROADCAST_STOP_TRACKING
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                if (value == 1) {
                    Log.i(TAG, "value == 1")
                    trackUser(userId, clientId, name, email)
                }
            }

        fun activityStarted() {
            numStarted++
        }

        fun activityStopped() {
            numStarted--
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

        }

        override fun onActivityStarted(activity: Activity?) {

        }

        override fun onActivityResumed(activity: Activity?) {
            activityStarted()
        }

        override fun onActivityPaused(activity: Activity?) {
            activityStopped()
        }

        override fun onActivityStopped(activity: Activity?) {

        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

        }

        override fun onActivityDestroyed(activity: Activity?) {

        }
    })
}

companion object {
    private var tracker: Tracker? = null
    fun getInstance(application: Application): Tracker {
        if (tracker == null) tracker = Tracker(application)
        return tracker as Tracker
    }
}
}
