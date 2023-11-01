package com.example.falcone_finder.framework.presentation.falcone_selection

sealed interface FalconeSelectionScreenEvent {
    data object onNext : FalconeSelectionScreenEvent
    data object onPrev : FalconeSelectionScreenEvent
    data object onPrevConfirm : FalconeSelectionScreenEvent
    data class onPlanetSelection(val planetIndex: Int, val planet: String) :
        FalconeSelectionScreenEvent

    data class onVehicleSelection(val vehicleIndex: Int, val vehicle: String) :
        FalconeSelectionScreenEvent
}