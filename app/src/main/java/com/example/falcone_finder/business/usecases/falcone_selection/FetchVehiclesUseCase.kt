package com.example.falcone_finder.business.usecases.falcone_selection

import com.example.falcone_finder.business.data.network.implementation.FalconeNetworkDataSourceImpl
import com.example.falcone_finder.business.domain.model.VehiclesApiResponse
import javax.inject.Inject

class FetchVehiclesUseCase @Inject constructor(private val networkDataSourceImpl: FalconeNetworkDataSourceImpl) {
    suspend operator fun invoke(): Result<List<VehiclesApiResponse>> {
        return networkDataSourceImpl.getVehicles()
    }
}