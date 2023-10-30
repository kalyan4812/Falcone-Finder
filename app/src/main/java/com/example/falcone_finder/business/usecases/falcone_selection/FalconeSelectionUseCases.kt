package com.example.falcone_finder.business.usecases.falcone_selection

import javax.inject.Inject

data class FalconeSelectionUseCases @Inject constructor(
    val fetchPlanetsUseCase: FetchPlanetsUseCase,
    val fetchTokenUseCase: FetchTokenUseCase,
    val fetchVehiclesUseCase: FetchVehiclesUseCase,
    val destinationStackUseCase: FalconeDestinationStackUseCase
)
