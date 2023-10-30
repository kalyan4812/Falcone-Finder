package com.example.falcone_finder.business.data.network.abstraction


import com.example.falcone_finder.business.domain.model.FindApiResponse
import com.example.falcone_finder.business.data.model.PlanetsApiResponse
import com.example.falcone_finder.business.domain.model.TokenApiResponse
import com.example.falcone_finder.business.domain.model.VehiclesApiResponse
import com.example.falcone_finder.business.domain.models.FindResponse
import com.example.falcone_finder.business.domain.models.Vehicle
import com.example.findingfalcone.domain.model.Planet


interface FalconeNetworkDataSource {

    suspend fun getToken(): Result<TokenApiResponse>


    suspend fun getPlanets(): Result<List<PlanetsApiResponse>>


    suspend fun getVehicles(): Result<List<VehiclesApiResponse>>


    suspend fun findPrincess(planets: List<String>?, vehicles: List<String>?): Result<FindApiResponse>
}