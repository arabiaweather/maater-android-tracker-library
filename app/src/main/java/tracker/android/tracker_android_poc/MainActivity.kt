package tracker.android.tracker_android_poc

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.maaterandroidtracker.Tracker

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun trackMe(v: View){

        this.getSharedPreferences("userdefaults", Context.MODE_PRIVATE).edit().apply {
            this.putInt("USER_ID",99)
            this.putInt("CLIENT_ID",7979)
            this.putString("NAME","donatello")
            this.putString("EMAIL","domnn@renesance.com")
        }.apply()
        Tracker.getInstance(application).trackUser(99,7878,"donatello", "domnn@renesance.com")
    }

}
