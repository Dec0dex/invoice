package net.decodex.invoice.api

import net.decodex.invoice.domain.model.Product
import retrofit2.http.*

interface ProductApi {

    @PUT("product")
    suspend fun update(@Body product: Product): Product

    @POST("product")
    suspend fun create(@Body product: Product): Product

    @DELETE("product/{id}")
    suspend fun delete(@Path("id") productId: Long)

    @GET("product/findAll")
    suspend fun findAllForCompanyId(@Query("company.id") companyId: Long): List<Product>
}