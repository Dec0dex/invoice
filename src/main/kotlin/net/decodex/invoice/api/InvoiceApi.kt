package net.decodex.invoice.api

import net.decodex.invoice.domain.dto.CreateInvoiceProductDto
import net.decodex.invoice.domain.model.Client
import net.decodex.invoice.domain.model.Invoice
import net.decodex.invoice.domain.model.Product
import retrofit2.http.*

interface InvoiceApi {

    @PUT("invoice")
    suspend fun update(@Body client: Client): Invoice

    @POST("invoice")
    suspend fun create(@Body client: Client): Invoice

    @DELETE("invoice/{id}")
    suspend fun delete(@Path("id") invoiceId: Long)

    @GET("invoice/findAll")
    suspend fun findAllForCompanyId(@Query("company.id") companyId: Long): List<Invoice>

    @POST("invoice/{id}/product")
    suspend fun createInvoiceProduct(@Path("id") invoiceId: Long, @Body dto: CreateInvoiceProductDto): Product

    @PUT("invoice/{id}/product")
    suspend fun updateInvoiceProduct(@Path("id") invoiceId: Long, @Body dto: CreateInvoiceProductDto): Product

    @DELETE("invoice/{id}/product/{productId}")
    suspend fun deleteInvoiceProduct(@Path("id") invoiceId: Long, @Path("productId") productPriceId: Long)

    @GET("invoice/{id}/product")
    suspend fun findAllProductsByInvoiceId(@Path("id") invoiceId: Long): List<Product>

}