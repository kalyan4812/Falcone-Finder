package com.example.falcone_finder.business.usecases.falcone_selection

import com.example.falcone_finder.business.data.model.PlanetsApiResponse
import com.example.falcone_finder.business.data.network.implementation.FalconeNetworkDataSourceImpl
import javax.inject.Inject

class FetchPlanetsUseCase @Inject constructor(private val networkDataSourceImpl: FalconeNetworkDataSourceImpl) {
    suspend operator fun invoke(): Result<List<PlanetsApiResponse>> {
        return networkDataSourceImpl.getPlanets()
    }
}