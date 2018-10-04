package tracker.android.tracker_android_poc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun trackMe(v: View){
        (applicationContext as MainApplication).track(99,7878,"donatello", "domnn@renesance.com")
    }

}
