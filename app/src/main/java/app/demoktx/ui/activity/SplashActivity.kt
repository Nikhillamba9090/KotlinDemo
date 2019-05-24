package app.demoktx.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler

import app.demoktx.R

// Starting activity
class SplashActivity : BaseActivity() {
    internal var handler: Handler? = null

    internal var runnable: Runnable = Runnable {
        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        finish()
    }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    /*
    * page will display for 2 sec.
    * */
     override fun onResume() {
        super.onResume()
        if (handler == null) {
            handler = Handler()
            handler!!.postDelayed(runnable, 2000)
        }
    }

     override fun onPause() {
        super.onPause()
        handler!!.removeCallbacksAndMessages(null)
        handler = null
    }
}
