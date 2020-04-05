package net.decodex.invoice.api

import kotlinx.coroutines.Deferred
import net.decodex.invoice.model.dto.AuthRequestDTO
import retrofit2.http.Body
import retrofit2.http.POST
import net.decodex.invoice.model.dto.AuthResponseDTO

interface AuthenticationApi {

    @POST("auth")
    fun authorize(@Body dto: AuthRequestDTO): Deferred<AuthResponseDTO>

}