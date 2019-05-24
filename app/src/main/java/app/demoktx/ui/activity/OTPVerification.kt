package app.demoktx.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.TextView

import com.chaos.view.PinView

import app.demoktx.R
import app.demoktx.utils.FunctionHelper
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged

class OTPVerification : BaseActivity() {
    internal lateinit var from: String
    @BindView(R.id.pinview_otp)
    lateinit var otpEt: PinView
    @BindView(R.id.desc_tv)
    lateinit var descTv: TextView

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        ButterKnife.bind(this)
        from = getIntent().getStringExtra("from")
        descTv.text = getString(R.string.otp_first_text)+" " + getIntent().getStringExtra("country_code") + "****" + getIntent().getStringExtra("last_two_number") +
                ", "+getString(R.string.otp_last_text)
    }

    // text change Listner on Edittext to check if input charecterd are
    // equal to 6 then check entered OTP is correct or not
    @OnTextChanged(R.id.pinview_otp)
    public fun textChanged() {
        if (otpEt!!.text!!.toString().length == 6) {
            checkAndMove()
        }
    }


    // click listener on verify button
    @OnClick(R.id.verify_button)
    fun verifyClicked() {
        if (otpEt!!.text!!.toString().length == 6) {
            checkAndMove()
        } else {
            FunctionHelper.hideKeyBoard(this, otpEt!!.rootView)
            FunctionHelper.showSnackMessage(otpEt!!.rootView, getString(R.string.otp_not_valid))
        }

    }

    private fun checkAndMove() {
        if (from == "sign_up") {
            moveToHome()
        } else if (from == "forgot") {
            moveToChangePassword()
        }
    }

    private fun moveToChangePassword() {
        val intent = Intent(this, ChangePasswordActivity::class.java)
        startActivity(intent)
    }

    // Start home activity
    private fun moveToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

}
