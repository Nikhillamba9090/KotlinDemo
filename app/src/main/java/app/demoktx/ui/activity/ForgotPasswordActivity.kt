package app.demoktx.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText

import com.hbb20.CountryCodePicker

import app.demoktx.R
import app.demoktx.utils.FunctionHelper
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class ForgotPasswordActivity : BaseActivity() {

    @BindView(R.id.number_et)
    lateinit var numberEt: EditText
    @BindView(R.id.ccp)
    lateinit var ccp: CountryCodePicker

    override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        ButterKnife.bind(this)
    }

    // click listener on reset button
    // check number is not empty or valid min: 5 max: 15
    @OnClick(R.id.reset_button)
    internal fun resetButtonClicked() {
        if (TextUtils.isEmpty(numberEt!!.text.toString())) {
            FunctionHelper.hideKeyBoard(this, numberEt!!.rootView)
            FunctionHelper.showSnackMessage(numberEt!!.rootView, "Please enter number.")
        } else if (!FunctionHelper.isNumberValid(numberEt!!.text.toString())) {
            FunctionHelper.hideKeyBoard(this, numberEt!!.rootView)
            FunctionHelper.showSnackMessage(numberEt!!.rootView, getString(R.string.number_not_valid))
        } else {
            val number = numberEt!!.text.toString()
            val intent = Intent(this, OTPVerification::class.java)
            intent.putExtra("from", "forgot")
            intent.putExtra("country_code", ccp!!.selectedCountryCodeWithPlus)
            intent.putExtra("last_two_number", number.substring(number.length - 4))

            startActivity(intent)
        }
    }
}
