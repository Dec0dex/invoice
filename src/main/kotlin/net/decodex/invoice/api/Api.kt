package net.decodex.invoice.api

import com.google.gson.GsonBuilder
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
    var companyApi = getRetrofitInstance(serverUrl).create(CompanyApi::class.java)
    var unitOfMeasureApi = getRetrofitInstance(serverUrl).create(UnitOfMeasureApi::class.java)
    var clientApi = getRetrofitInstance(serverUrl).create(ClientApi::class.java)
    var productApi = getRetrofitInstance(serverUrl).create(ProductApi::class.java)
    var invoiceApi = getRetrofitInstance(serverUrl).create(InvoiceApi::class.java)

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
            .build()
    }
}