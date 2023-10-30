package com.example.falcone_finder.framework.presentation.falcone_selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falcone_finder.business.data.model.PlanetsApiResponse
import com.example.falcone_finder.business.domain.model.VehiclesApiResponse
import com.example.falcone_finder.business.domain.models.FalconeFindingData
import com.example.falcone_finder.business.domain.models.FalconeSelectionState
import com.example.falcone_finder.business.usecases.falcone_selection.FalconeDestinationStackUseCase
import com.example.falcone_finder.business.usecases.falcone_selection.FalconeSelectionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class FalconeSelectionViewModel @Inject constructor(
    private val falconeSelectionUseCases: FalconeSelectionUseCases
) :
    ViewModel() {


    private val _planetsData: MutableLiveData<List<PlanetsApiResponse>> = MutableLiveData()
    val planetsData = _planetsData
    private val _vehiclesData: MutableLiveData<List<VehiclesApiResponse>> = MutableLiveData()
    val vehiclesData = _vehiclesData

    private val _selectionData: MutableLiveData<Triple<Int, Int, Int>> = MutableLiveData()
    val selectionData = _selectionData

    private val _uiEvents = Channel<FalconeScreenUIEvent>()
    val uiEvents = _uiEvents.receiveAsFlow()

    private var currentPlanetIndex: Int = 0
    private var currentVehicleIndex: Int = 0
    private var currentPlanetSelection: String? = ""
    private var currentVehicleSelection: String? = ""


    init {
        initToken()
        getPlanetsData()
        getVehiclesData()
        sendUiEvent(FalconeScreenUIEvent.refreshUI(1, null))
    }


    fun onEventRecieved(event: FalconeSelectionScreenEvent) {
        when (event) {
            is FalconeSelectionScreenEvent.onNext -> {
                falconeSelectionUseCases.destinationStackUseCase.pushSelection(
                    currentPlanetIndex,
                    currentPlanetSelection,
                    currentVehicleIndex,
                    currentVehicleSelection
                )
                if (falconeSelectionUseCases.destinationStackUseCase.isMaxLimitReached()) {
                    sendUiEvent(FalconeScreenUIEvent.navigateToFindPrincess(falconeSelectionUseCases.destinationStackUseCase.getFindRequestFromStack()))
                    falconeSelectionUseCases.destinationStackUseCase.clearStack()
                } else {
                    sendUiEvent(
                        FalconeScreenUIEvent.refreshUI(
                            falconeSelectionUseCases.destinationStackUseCase.currentSize() + 1,
                            null,
                            falconeSelectionUseCases.destinationStackUseCase.currentSize() ==
                                    falconeSelectionUseCases.destinationStackUseCase.maxPlanets() - 1
                        )
                    )
                }
            }

            is FalconeSelectionScreenEvent.onPlanetSelection -> {
                currentPlanetIndex = event.planetIndex
                currentPlanetSelection = event.planet
            }

            FalconeSelectionScreenEvent.onPrev -> {
                sendUiEvent(
                    FalconeScreenUIEvent.prevConfirmDialog(
                        falconeSelectionUseCases.destinationStackUseCase.currentSize()
                    )
                )
            }

            FalconeSelectionScreenEvent.onPrevConfirm -> {
                if (falconeSelectionUseCases.destinationStackUseCase.currentSize() == 0) return
                sendUiEvent(
                    FalconeScreenUIEvent.refreshUI(
                        falconeSelectionUseCases.destinationStackUseCase.currentSize(),
                        Pair(
                            falconeSelectionUseCases.destinationStackUseCase.peekSelection().planetIndex,
                            falconeSelectionUseCases.destinationStackUseCase.peekSelection().vehicleIndex
                        )
                    )
                )
                falconeSelectionUseCases.destinationStackUseCase.popSelection()
            }

            is FalconeSelectionScreenEvent.onVehicleSelection -> {
                currentVehicleSelection = event.vehicle
                currentVehicleIndex = event.vehicleIndex
            }
        }
    }

    private fun sendUiEvent(event: FalconeScreenUIEvent) {
        viewModelScope.launch {
            _uiEvents.send(event)
        }
    }

    private fun initToken() {
        viewModelScope.launch(Dispatchers.IO) {
            falconeSelectionUseCases.fetchTokenUseCase.invoke()
        }
    }

    private fun getPlanetsData() {
        viewModelScope.launch(Dispatchers.IO) {
            sendUILoaderEvent(true)
            val data = falconeSelectionUseCases.fetchPlanetsUseCase.invoke()
            data.onSuccess {
                sendUILoaderEvent()
                _planetsData.postValue(it)
            }.onFailure {
                sendUILoaderEvent()
                sendUiEvent(FalconeScreenUIEvent.showTost(it.localizedMessage ?: ""))
            }
        }
    }

    private fun getVehiclesData() {
        viewModelScope.launch(Dispatchers.IO) {
            sendUILoaderEvent(true)
            val data = falconeSelectionUseCases.fetchVehiclesUseCase.invoke()
            data.onSuccess {
                sendUILoaderEvent()
                _vehiclesData.postValue(it)
            }.onFailure {
                sendUILoaderEvent()
                sendUiEvent(FalconeScreenUIEvent.showTost(it.localizedMessage ?: ""))
            }
        }
    }

    private fun sendUILoaderEvent(show: Boolean = false) {
        sendUiEvent(FalconeScreenUIEvent.progressBarStatus(show))
    }

    fun populatePlanetAndVehicleIndexs() {
        _selectionData.postValue(
            Triple(
                currentPlanetIndex,
                currentVehicleIndex,
                falconeSelectionUseCases.destinationStackUseCase.currentSize() + 1
            )
        )
    }

}