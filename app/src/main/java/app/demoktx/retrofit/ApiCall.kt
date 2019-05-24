package app.demoktx.retrofit

import app.demoktx.retrofit.response.LoginSignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCall {

    fun userLogin(cCode: String, phoneNum: String, password: String, version: String, userType: String, iApiCallback: IApiCallback) {
        val call = service!!.login(cCode, phoneNum, password, version, userType)
        call.enqueue(object : Callback<LoginSignUpResponse> {
            override fun onResponse(call: Call<LoginSignUpResponse>, response: Response<LoginSignUpResponse>) {
                iApiCallback.onSuccess("login", response, "")
            }

            override fun onFailure(call: Call<LoginSignUpResponse>, t: Throwable) {
                iApiCallback.onFailure("" + t.message)
            }
        })
    }

    companion object {

        private var service: APIService? = null

        val instance: ApiCall
            get() {
                if (service == null) {
                    service = RestClient.client
                }
                return ApiCall()
            }
    }
}
