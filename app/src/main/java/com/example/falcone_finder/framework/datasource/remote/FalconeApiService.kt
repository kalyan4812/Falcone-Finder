package com.example.falcone_finder.framework.datasource.remote

import com.example.falcone_finder.business.domain.model.FindApiRequest
import com.example.falcone_finder.business.domain.model.FindApiResponse
import com.example.falcone_finder.business.data.model.PlanetsApiResponse
import com.example.falcone_finder.business.domain.model.TokenApiResponse
import com.example.falcone_finder.business.domain.model.VehiclesApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface FalconeApiService {

    @Headers(
        "Accept: application/json")
    @POST("/token")
    suspend fun getToken(): TokenApiResponse

    @GET("/planets")
    suspend fun getPlanets(): List<PlanetsApiResponse>

    @GET("/vehicles")
    suspend fun getVehicles(): List<VehiclesApiResponse>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("/find")
    suspend fun findPrincess(@Body body: FindApiRequest): FindApiResponse
}