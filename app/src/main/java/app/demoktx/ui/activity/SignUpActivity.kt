package app.demoktx.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.hbb20.CountryCodePicker

import app.demoktx.R
import app.demoktx.utils.CheckConnection
import app.demoktx.utils.FunctionHelper
import app.demoktx.utils.RealPathUtils
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import java.util.ArrayList

class SignUpActivity : BaseActivity() {

    @BindView(R.id.full_name_et)
    lateinit var fullNameEt: EditText
    @BindView(R.id.number_et)
    lateinit var numberEt: EditText
    @BindView(R.id.email_et)
    lateinit var emailEt: EditText
    @BindView(R.id.password_et)
    lateinit var passwordEt: EditText
    @BindView(R.id.cnfrm_password_et)
    lateinit var cnfrmPasswordEt: EditText
    @BindView(R.id.reference_et)
    lateinit var referenceEt: EditText
    @BindView(R.id.ccp)
    lateinit var ccp: CountryCodePicker
    var PICK_IMAGE_FROM_GALLERY = 2121
    var PICK_IMAGE_FROM_CAMERA = 2020
    private lateinit var fileUri: Uri
    private var path: String? = null;

    internal var permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    private val isValid: Boolean
        get() {
            if (CheckConnection.isConnection(this)) {
                if (TextUtils.isEmpty(fullNameEt.text.toString())) {
                    numberEt.requestFocus()
                    FunctionHelper.hideKeyBoard(this, referenceEt.rootView)
                    FunctionHelper.showSnackMessage(fullNameEt.rootView, getString(R.string.full_name_cannot_empty))
                    return false
                } else if (TextUtils.isEmpty(numberEt.text.toString())) {
                    numberEt.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.number_cannot_empty))
                    return false

                } else if (!FunctionHelper.isNumberValid(numberEt.text.toString())) {
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.number_not_valid))
                    return false
                } else if (TextUtils.isEmpty(emailEt.text.toString())) {
                    emailEt.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.email_cannot_empty))
                    return false
                } else if (!FunctionHelper.isValidEmail(emailEt.text.toString())) {
                    emailEt.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.email_not_valid))
                    return false
                } else if (TextUtils.isEmpty(passwordEt.text.toString())) {
                    passwordEt.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.password_cannot_empty))
                    return false
                } else if (TextUtils.isEmpty(cnfrmPasswordEt.text.toString())) {
                    passwordEt.rootView.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.cnfrm_password_cannot_empty))
                    return false
                } else if (passwordEt.text.toString() != cnfrmPasswordEt.text.toString()) {
                    passwordEt.rootView.requestFocus()
                    FunctionHelper.hideKeyBoard(this, passwordEt.rootView)
                    FunctionHelper.showSnackMessage(numberEt.rootView, getString(R.string.password_not_matched))
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
        setContentView(R.layout.activity_sign_up)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.label_or, R.id.label_login)
    internal fun moveToBack() {
        onBackPressed()
    }

    /*
    * Create new user
    * */
    @OnClick(R.id.sign_up_button)
    internal fun moveToOTPVerification() {
        if (isValid) {
            val intent = Intent(this, OTPVerification::class.java)
            intent.putExtra("from", "sign_up")
            intent.putExtra("country_code", ccp.selectedCountryCodeWithPlus)
            intent.putExtra("last_two_number", numberEt.text.toString().substring(numberEt.text.toString().length - 4))
            startActivity(intent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_FROM_CAMERA) {
            path = RealPathUtils.getRealPath(this, fileUri);
        }
    }


}
