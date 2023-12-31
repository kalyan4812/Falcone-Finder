package com.example.falcone_finder.business.usecases.falcone_selection

import com.example.falcone_finder.business.data.network.implementation.FalconeNetworkDataSourceImpl
import com.example.falcone_finder.business.domain.model.TokenApiResponse
import javax.inject.Inject

class FetchTokenUseCase @Inject constructor(private val networkDataSourceImpl: FalconeNetworkDataSourceImpl) {
    suspend operator fun invoke(): Result<TokenApiResponse> {
        return networkDataSourceImpl.getToken()
    }
}