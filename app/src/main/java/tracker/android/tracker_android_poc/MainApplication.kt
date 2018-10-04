package tracker.android.tracker_android_poc

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

class MainApplication : Application() {

    val TAG = "MainApplication"

    public fun track(userId: Int, clientId: Int, name: String, email: String) {
        val i = Intent(this, TrackingService::class.java)
        i.apply{
            this.putExtra(USER_ID,userId)
            this.putExtra(CLIENT_ID,clientId)
            this.putExtra(NAME,name)
            this.putExtra(EMAIL,email)
        }
        this.startService(i)
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            private var numStarted: Int = 0
            set(value) {
                if (value <= 0){
                    Log.i(TAG,"value <= 0")
                    val intent = Intent()
                    intent.action = BROADCAST_STOP_TRACKING
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                }
                if (value == 1){
                    Log.i(TAG,"value == 1")
                    (applicationContext as MainApplication).track(99,7878,"donatello", "domnn@renesance.com")
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

}
