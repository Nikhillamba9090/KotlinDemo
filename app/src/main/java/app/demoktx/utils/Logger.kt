package app.demoktx.utils

import android.util.Log

/**
 * Created by appsinvo on 6/8/18.
 */


object Logger {
    private val isShowLog = true
    fun d(data: String) {
        if (isShowLog) {
            Log.d("Logger", data)
        }
    }

    fun res(data: String) {
        if (isShowLog) {
            Log.d("Response", data)
        }
    }

}
