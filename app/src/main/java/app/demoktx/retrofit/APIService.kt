package app.demoktx.retrofit


import app.demoktx.retrofit.response.LoginSignUpResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by appsinvo on 13/11/18.
 */

interface APIService {

    @POST("login")
    @FormUrlEncoded
    fun login(
            @Field("country_code") cCode: String,
            @Field("phone") phone: String,
            @Field("password") password: String,
            @Field("user_type") userType: String,
            @Field("version_key") versionKey: String): Call<LoginSignUpResponse>


}
