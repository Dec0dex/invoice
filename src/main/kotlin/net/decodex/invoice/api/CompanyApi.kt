package net.decodex.invoice.api

import kotlinx.coroutines.Deferred
import net.decodex.invoice.domain.model.Company
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CompanyApi {

    @GET("company/{id}")
    fun getCompanyInfo(@Path("id") companyId: Long): Deferred<Company>

    @PUT("company")
    fun updateCompany(@Body company: Company): Deferred<Company>

}