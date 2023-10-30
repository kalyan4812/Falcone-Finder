package com.example.falcone_finder.business.usecases.falcone_finding

import com.example.falcone_finder.business.data.network.implementation.FalconeNetworkDataSourceImpl
import com.example.falcone_finder.business.domain.model.FindApiResponse
import com.example.falcone_finder.business.domain.models.FindResponse
import javax.inject.Inject

class FindPrincessUseCase @Inject constructor(private val networkDataSourceImpl: FalconeNetworkDataSourceImpl) {
    suspend operator fun invoke(
        planets: List<String>?,
        vehicles: List<String>?
    ): Result<FindResponse> {
        return networkDataSourceImpl.findPrincess(planets, vehicles).map {
            it.mapToFindResponse()
        }
    }

    private fun FindApiResponse.mapToFindResponse() =
        when (status) {
            "success" -> FindResponse.Success(planetName!!)
            "false" -> FindResponse.Failure
            else -> throw SecurityException(error)
        }
}

