package net.decodex.invoice.api

import net.decodex.invoice.domain.dto.AuthRequestDTO
import net.decodex.invoice.domain.dto.AuthResponseDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("auth")
    suspend fun authorize(@Body dto: AuthRequestDTO): AuthResponseDTO

}