package app.demoktx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task


import java.net.URL
import java.util.Arrays
import java.util.concurrent.Executor

import app.demoktx.R
import app.demoktx.retrofit.IApiCallback
import app.demoktx.utils.FunctionHelper
import app.demoktx.utils.Logger

import com.facebook.FacebookSdk.getApplicationContext

class SocialLoginFragment : Fragment(), View.OnClickListener, IApiCallback {

    private var google_login: ImageView? = null
    private var fb_login: ImageView? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 100
    private var id: String? = null
    private var full_name: String? = null
    private var email_add: String? = null
    private val mobile_no: String? = null
    private var mAccessToken: AccessToken? = null
    private var callbackManager: CallbackManager? = null
    private var type_val: String? = null
    private var url: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_social_login, container, false)
        findView(view)

        return view
    }

    private fun findView(view: View) {
        google_login = view.findViewById(R.id.google_login)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)
        google_login!!.setOnClickListener(this)

        callbackManager = CallbackManager.Factory.create()

        fb_login = view.findViewById(R.id.fb_login)
        fb_login!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.google_login -> google_signIn()

            R.id.fb_login -> fb_signIn()
        }

    }

    private fun showToast(mess: String) {
        Toast.makeText(context, mess, Toast.LENGTH_SHORT).show()
    }


    private fun google_signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun fb_signIn() {

        LoginManager.getInstance().registerCallback(callbackManager!!, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                mAccessToken = loginResult.accessToken
                getUserProfile(mAccessToken)
            }

            override fun onCancel() {}


            override fun onError(exception: FacebookException) {
                showToast("Facebook Login Error")
            }
        })

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

        callbackManager!!.onActivityResult(requestCode, resultCode, data)

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        try {
            val account = completedTask.getResult<ApiException>(ApiException::class.java!!)
            updateUI(account)

        } catch (e: ApiException) {
            Logger.d("Sign in " + e.statusCode)
            updateUI(null)
        }

    }

    private fun updateUI(account: GoogleSignInAccount?) {

        if (account != null) {

            Logger.d("1234")

            id = account.id
            full_name = account.displayName
            email_add = account.email
            type_val = "google"
            url = "" + account.photoUrl!!
            requestData(email_add, id, "google")
        } else {
        }
    }

    private fun requestData(email: String?, id: String?, type: String?) {

        /* LoginRequest request = new LoginRequest();

        request.setEmailPhone(email);

        if(type.equals("google")) {
            request.setLoginType(3);
            request.setGoogleId(id);
        }

        if(type.equals("facebook")) {
            request.setLoginType(2);
            request.setFacebookId(id);
        }

        progressDialog.show();
        ApiCall.getInstance().login(request, this);*/
    }

    private fun getUserProfile(mAccessToken: AccessToken?) {

        val request = GraphRequest.newMeRequest(
                mAccessToken
        ) { `object`, response ->
            try {
                val facebookId = `object`.getString("id")
                val name = `object`.getString("name")
                var email = ""
                val imageUrl = ""
                try {
                    email = `object`.getString("email")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    val imgUrl = URL("https://graph.facebook.com/$facebookId/picture?type=large")
                    url = imgUrl.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                email_add = email
                id = facebookId
                full_name = name
                type_val = "facebook"
                requestData(email_add, id, type_val)


            } catch (e: Exception) {
                e.printStackTrace()
                //                        }

            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id,name,link")
        request.parameters = parameters
        request.executeAsync()
    }

    override
    fun onSuccess(type: Any, data: Any, extraData: Any) {
        /*progressDialog.dismiss();
        Response<LoginResponse> response = (Response<LoginResponse>) data;
        if (response.isSuccessful()) {

            String errorCode = response.body().getErrorCode();

            StoreUserData storeUserData = new StoreUserData();

            Logger.show("@@@@ " + errorCode);

            if (errorCode.equals("2")) {

                Intent intent = new Intent(getActivity(), SignUpActivity.class);
                intent.putExtra("name", full_name);
                intent.putExtra("email", email_add);
                intent.putExtra("id", id);
                intent.putExtra("type",type_val);


                if (url != null)
                    intent.putExtra("uri", url);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            else if(errorCode.equals("0")) {
                Intent intent = new Intent(getActivity() , HomeScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }*/
    }

     override
    fun onFailure(data: Any) {
        Logger.d("@@@@ $data")
    }

    fun google_signOut() {
        try {
            mGoogleSignInClient!!.signOut()
                    .addOnCompleteListener(this as Executor, OnCompleteListener {
                        Toast.makeText(getApplicationContext(), "You have signed out from your google account. Please Sign in Again", Toast.LENGTH_LONG)
                                .show()
                    })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun fb_signOut() {
        google_signOut()
        LoginManager.getInstance().logOut()
    }

}