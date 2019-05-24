package app.demoktx.utils

import android.content.Context
import android.net.ConnectivityManager


object CheckConnection {

    fun isConnection(ctx: Context): Boolean {
        val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val ni = connectivityManager.activeNetworkInfo
        return if (ni != null && ni.isAvailable && ni.isConnected) {
            true
        } else {
            false
        }
    }
}