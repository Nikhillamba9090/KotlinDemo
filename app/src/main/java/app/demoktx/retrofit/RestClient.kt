package app.demoktx.retrofit


import com.google.gson.GsonBuilder

import java.util.concurrent.TimeUnit

import app.demoktx.BuildConfig
import app.demoktx.constants.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RestClient {


    private var apiRestInterfaces: APIService? = null


    val client: APIService?
        get() {


            val okHttpClient = OkHttpClient.Builder()
                    .readTimeout(180, TimeUnit.SECONDS)
                    .connectTimeout(180, TimeUnit.SECONDS)


            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                okHttpClient.addInterceptor(interceptor)
            }

            val gson = GsonBuilder()
                    .setLenient()
                    .create()


            if (apiRestInterfaces == null) {
                val client = Retrofit.Builder()
                        .baseUrl(Constants.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(okHttpClient.build())
                        .build()
                apiRestInterfaces = client.create(APIService::class.java!!)
            }
            return apiRestInterfaces
        }


}
