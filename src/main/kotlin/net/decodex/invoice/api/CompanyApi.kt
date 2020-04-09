package net.decodex.invoice.api

import net.decodex.invoice.domain.model.Company
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CompanyApi {

    @GET("company/{id}")
    suspend fun getCompanyInfo(@Path("id") companyId: Long): Company

    @PUT("company")
    suspend fun updateCompany(@Body company: Company): Company

}