package app.demoktx.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText

import app.demoktx.R
import app.demoktx.utils.CheckConnection
import app.demoktx.utils.FunctionHelper
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class ChangePasswordActivity : BaseActivity() {

    @BindView(R.id.password_et)
    lateinit var passwordEt: EditText
    @BindView(R.id.cnfrm_password_et)
    lateinit var cnfrmPasswordEt: EditText


    // validation to check required fields should not be empty
    // and well formatted
    private val isValid: Boolean
        get() {
            // Check Device is connected to internet or not
            if (CheckConnection.isConnection(this)) {
                if (TextUtils.isEmpty(passwordEt!!.text.toString())) {
                    passwordEt!!.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt!!.rootView)
                    FunctionHelper.showSnackMessage(passwordEt!!.rootView, getString(R.string.password_cannot_empty))
                    return false
                } else if (TextUtils.isEmpty(cnfrmPasswordEt!!.text.toString())) {
                    passwordEt!!.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt!!.rootView)
                    FunctionHelper.showSnackMessage(passwordEt!!.rootView, getString(R.string.cnfrm_password_cannot_empty))
                    return false
                } else if (passwordEt!!.text.toString() != cnfrmPasswordEt!!.text.toString()) {
                    passwordEt!!.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt!!.rootView)
                    FunctionHelper.showSnackMessage(passwordEt!!.rootView, getString(R.string.password_not_matched))
                    return false
                } else {
                    FunctionHelper.hideKeyBoard(this, passwordEt!!.rootView)
                    return true
                }
            } else {
                FunctionHelper.hideKeyBoard(this, passwordEt!!.rootView)
                FunctionHelper.showSnackMessage(passwordEt!!.rootView, getString(R.string.no_internet))
                return false
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.change_password_button)
    internal fun changePasswordClicked() {
        if (isValid) {
            moveToHome()
        }
    }

    // Start home activity
    private fun moveToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}
