package net.decodex.invoice.api

import net.decodex.invoice.domain.model.User
import retrofit2.http.GET

interface UserApi {

    @GET("user")
    suspend fun getAuthenticatedUserInfo(): User

}