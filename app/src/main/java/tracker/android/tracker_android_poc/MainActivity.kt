package tracker.android.tracker_android_poc

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        track(99,7878,"donatello", "domnn@renesance.com")
    }

    fun track(userId: Int, clientId: Int, name: String, email: String) {
        val i = Intent(this, TrackingService::class.java)
        i.apply{
            this.putExtra(USER_ID,3)
            this.putExtra(CLIENT_ID,3)
            this.putExtra(NAME,"Jonger")
            this.putExtra(EMAIL,"Jonger@grendizer.com")
        }
        this.startService(i)
    }

}
