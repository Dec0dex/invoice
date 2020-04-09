package net.decodex.invoice.api

import net.decodex.invoice.domain.model.UnitOfMeasure
import retrofit2.http.*

interface UnitOfMeasureApi {

    @GET("uom/findAll")
    suspend fun findAll(@Query("company.id") companyId: Long): List<UnitOfMeasure>

    @POST("uom")
    suspend fun createUnitOfMeasure(@Body uom: UnitOfMeasure): UnitOfMeasure

    @PUT("uom")
    suspend fun updateUnitOfMeasure(@Body uom: UnitOfMeasure): UnitOfMeasure

    @DELETE("uom/{id}")
    suspend fun deleteUnitOfMeasure(@Path("id") id: Long)
}