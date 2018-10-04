package tracker.android.tracker_android_poc

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.example.maaterandroidtracker.Tracker

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        resumeTrackUser()
    }

    fun resumeTrackUser(){
        val userId = this.getSharedPreferences("userdefaults", Context.MODE_PRIVATE).getInt("USER_ID",0)
        val clientId = this.getSharedPreferences("userdefaults", Context.MODE_PRIVATE).getInt("CLIENT_ID",0)
        val email = this.getSharedPreferences("userdefaults", Context.MODE_PRIVATE).getString("EMAIL","")
        val name = this.getSharedPreferences("userdefaults", Context.MODE_PRIVATE).getString("NAME","")
        Log.i("MainApplication","resumeTrackUser: userId: $userId, $clientId, $name, $email")
        Tracker.getInstance(this).resumeTrackingUser(userId,clientId,name,email)
    }

}
