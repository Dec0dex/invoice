package net.decodex.invoice.api

import kotlinx.coroutines.Deferred
import net.decodex.invoice.domain.model.User
import retrofit2.http.GET

interface UserApi {

    @GET("user")
    fun getAuthenticatedUserInfo(): Deferred<User>

}