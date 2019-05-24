package app.demoktx.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.FileProvider

import com.google.android.material.snackbar.Snackbar

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat

import app.demoktx.R
import app.demoktx.constants.Constants
import java.util.*

/**
 * Created by appsinvo on 1/8/18.
 */

object FunctionHelper {



    // hide keyboard
    fun hideKeyBoard(context: Context, editText: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun showSnackMessage(view: View, message: String) {
        try {


            val snackbar: Snackbar
            snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val snackBarView = snackbar.view
            snackBarView.setBackgroundColor(Color.parseColor("#026668"))
            val textView = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
            textView.setTextColor(Color.parseColor("#FFFFFF"))
            snackbar.setAction("Action", null)
            snackbar.show()


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // Check is number valid or not min:4 max 15
    fun isNumberValid(number: String): Boolean {
        return number.length<4||number.length>15
    }

    // Check mail is valid or not
    fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }



}
