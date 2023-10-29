package com.example.falcone_finder.business.data.network.implementation

import com.example.falcone_finder.application.SharedPrefHelper
import com.example.falcone_finder.business.data.model.PlanetsApiResponse
import com.example.falcone_finder.business.data.network.abstraction.FalconeNetworkDataSource
import com.example.falcone_finder.business.data.utils.safeApiCall
import com.example.falcone_finder.business.domain.model.FindApiRequest
import com.example.falcone_finder.business.domain.model.FindApiResponse
import com.example.falcone_finder.business.domain.model.TokenApiResponse
import com.example.falcone_finder.business.domain.model.VehiclesApiResponse
import com.example.falcone_finder.business.domain.models.FindResponse
import com.example.falcone_finder.framework.datasource.remote.FalconeApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FalconeNetworkDataSourceImpl @Inject constructor(
    private val falconeApiService: FalconeApiService, private val sharedPrefHelper: SharedPrefHelper
) : FalconeNetworkDataSource {

    override suspend fun getToken(): Result<TokenApiResponse> {
        return safeApiCall {
            falconeApiService.getToken().also {
                sharedPrefHelper.token = it.token
            }
        }
    }

    override suspend fun getPlanets(): Result<List<PlanetsApiResponse>> {
        return safeApiCall {
            falconeApiService.getPlanets()
        }
    }

    override suspend fun getVehicles(): Result<List<VehiclesApiResponse>> {
        return safeApiCall { falconeApiService.getVehicles() }
    }

    override suspend fun findPrincess(
        planets: List<String>?,
        vehicles: List<String>?
    ): Result<FindResponse> {
        return safeApiCall {
            falconeApiService.findPrincess(
                FindApiRequest(
                    sharedPrefHelper.token ?: "",
                    planets,
                    vehicles
                )
            ).mapToFindResponse()
        }
    }

}

fun FindApiResponse.mapToFindResponse() =
    when (status) {
        "success" -> FindResponse.Success(planetName!!)
        "false" -> FindResponse.Failure
        else -> throw SecurityException(error)
    }