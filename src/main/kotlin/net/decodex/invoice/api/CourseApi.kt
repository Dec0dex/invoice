package net.decodex.invoice.api

import net.decodex.invoice.domain.model.course.Course
import retrofit2.http.*

interface CourseApi {

    @PUT("course")
    suspend fun update(@Body course: Course): Course

    @POST("course")
    suspend fun create(@Body course: Course): Course

    @DELETE("course/{id}")
    suspend fun delete(@Path("id") courseId: Long)

    @GET("course/findAll")
    suspend fun findAllForCompanyId(@Query("company.id") companyId: Long): List<Course>

    @GET("course/{id}")
    suspend fun findById(@Path("id") courseId: Long): Course

}