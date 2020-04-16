package net.decodex.invoice.api

import net.decodex.invoice.domain.model.Client
import retrofit2.http.*

interface ClientApi {

    @PUT("client")
    suspend fun update(@Body client: Client): Client

    @POST("client")
    suspend fun create(@Body client: Client): Client

    @DELETE("client/{id}")
    suspend fun delete(@Path("id") clientId: Long)

    @GET("client/findAll")
    suspend fun findAllForCompanyId(@Query("company.id") companyId: Long): List<Client>

    @GET("client/{id}")
    suspend fun findById(@Path("id") clientId: Long): Client
}