package net.decodex.invoice.api

import net.decodex.invoice.api.Api.TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $TOKEN")
            .build()

        return chain.proceed(request)
    }
}