package com.example.falcone_finder.framework.presentation.falcone_selection

sealed class FalconeSelectionScreenEvent {
    object onNext : FalconeSelectionScreenEvent()
    object onPrev : FalconeSelectionScreenEvent()
    object onPrevConfirm : FalconeSelectionScreenEvent()
    data class onPlanetSelection(val planetIndex: Int,val planet:String) : FalconeSelectionScreenEvent()
    data class onVehicleSelection(val vehicleIndex: Int,val vehicle:String) : FalconeSelectionScreenEvent()
}