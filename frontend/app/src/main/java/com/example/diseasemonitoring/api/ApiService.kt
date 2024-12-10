package com.example.diseasemonitoring.api

import com.example.diseasemonitoring.models.Disease
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class RegisterRequest(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String,
    val age: Int
)

data class LoginRequest(val email: String, val password: String)
data class ApiResponse(val success: Boolean, val message: String)

interface ApiService {
    @POST("/register")
    suspend fun registerUser(@Body request: RegisterRequest): ApiResponse

    @POST("/login")
    suspend fun loginUser(@Body request: LoginRequest): ApiResponse

    @GET("api/diseases/user/{userId}/name/{name}")
    fun getDiseaseUserIdAndByName(
        @Path("userId") userId: String,
        @Path("name") name: String
    ): Call<Disease>

    @POST("/api/diseases")
    fun addDisease(@Body disease: Disease): Call<Disease>

    @GET("api/diseases/user/{userId}")
    fun getAllDiseases(@Path("userId") userId: String): Call<List<Disease>>

    @PUT("api/diseases/user/{userId}/name/{name}")
    fun updateDisease(
        @Path("userId") userId: String,
        @Path("name") name: String,
        @Body disease: Disease
    ): Call<Disease>


    @DELETE("api/diseases/user/{userId}/name/{name}")
    fun deleteDisease(
        @Path("userId") userId: String,
        @Path("name") name: String
    ): Call<Void>

}