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

class LoginActivity : BaseActivity() {

    @BindView(R.id.number_et)
    lateinit var numberEt: EditText
    @BindView(R.id.password_et)
    lateinit var passwordEt: EditText

    //to check required fields should be not empty
    private val isValid: Boolean
        get() {
            if (CheckConnection.isConnection(this)) {
                if (TextUtils.isEmpty(numberEt.text.toString())) {
                    numberEt.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.number_cannot_empty))
                    return false

                } else if (TextUtils.isEmpty(passwordEt.text.toString())) {
                    passwordEt.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.password_cannot_empty))
                    return false
                } else {
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    return true
                }

            } else {
                FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.no_internet))
                return false
            }
        }

    override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
    }

    // click on login button
    // isValid() is to check that required fields are not empty
    @OnClick(R.id.login_Button)
    internal fun loginClicked() {
        if (isValid) {
            moveToHome()

        }
    }


    //click listener on signup to go on Signup Activity
    @OnClick(R.id.label_or, R.id.label_signup)
    internal fun moveToSignUp() {
        if (checkNet())
            startActivity(Intent(this, SignUpActivity::class.java))

    }

    //click listener on forgot password to go on Forgot Password Activity
    @OnClick(R.id.forgot_password_tv)
    internal fun moveToForgot() {
        if (checkNet())
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))

    }
    // if valid then start home activity
    private fun moveToHome() {
        if (checkNet()) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
    }

    // check network is connected or not
    // if not connected then show error
    private fun checkNet(): Boolean {
        if (CheckConnection.isConnection(this)) {
            return true
        } else {
            FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
            FunctionHelper.showSnackMessage(passwordEt.rootView, getString(R.string.no_internet))
            return false
        }
    }


}
