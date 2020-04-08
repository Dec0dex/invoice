package net.decodex.invoice.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import net.decodex.invoice.utils.PropUtils
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Api {

    private var serverUrl = PropUtils.load("apiUrl") ?: ""
    var TOKEN: String = ""

    var authApi = getRetrofitInstance(serverUrl, getOkHttpClient()).create(AuthenticationApi::class.java)
    var userApi = getRetrofitInstance(serverUrl).create(UserApi::class.java)

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    private fun getOkHttpAuthClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(TokenInterceptor())
            .build()
    }

    private fun getGsonConverter(): Converter.Factory {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create()

        return GsonConverterFactory.create(gson)
    }

    private fun getRetrofitInstance(
        baseURL: String,
        client: OkHttpClient = getOkHttpAuthClient()
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURL)
            .client(client)
            .addConverterFactory(getGsonConverter())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .build()
    }
}