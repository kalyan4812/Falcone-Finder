package com.example.falcone_finder.framework.presentation.falcone_selection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falcone_finder.business.data.model.PlanetsApiResponse
import com.example.falcone_finder.business.data.network.implementation.FalconeNetworkDataSourceImpl
import com.example.falcone_finder.business.domain.model.FindApiRequest
import com.example.falcone_finder.business.domain.model.VehiclesApiResponse
import com.example.falcone_finder.business.domain.models.FalconeFindingData
import com.example.falcone_finder.business.domain.models.FalconeSelectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.Stack
import javax.inject.Inject

@HiltViewModel
class FalconeSelectionViewModel @Inject constructor(private val falconeNetworkDataSourceImpl: FalconeNetworkDataSourceImpl) :
    ViewModel() {


    private val _planetsData: MutableLiveData<List<PlanetsApiResponse>> = MutableLiveData()
    val planetsData = _planetsData
    private val _vehiclesData: MutableLiveData<List<VehiclesApiResponse>> = MutableLiveData()
    val vehiclesData = _vehiclesData

    private val _selectionData: MutableLiveData<Pair<Int, Int>> = MutableLiveData()
    val selectionData = _selectionData

    private val _uievents = Channel<FalconeScreenUIEvent>()
    val ui_events = _uievents.receiveAsFlow()

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

    fun populatePlanetAndVehicleIndexs() {
        _selectionData.postValue(Pair(currentPlanetIndex, currentVehicleIndex))
    }

    fun onEventRecieved(event: FalconeSelectionScreenEvent) {
        when (event) {
            is FalconeSelectionScreenEvent.onNext -> {
                pushSelection()
                if (selectionStack.size == 4) {
                    sendUiEvent(FalconeScreenUIEvent.navigateToFindPrincess(getFindRequestFromStack()))
                    selectionStack.clear()
                } else {
                    sendUiEvent(
                        FalconeScreenUIEvent.refreshUI(
                            selectionStack.size + 1, null, selectionStack.size == 3
                        )
                    )
                }
            }

            is FalconeSelectionScreenEvent.onPlanetSelection -> {
                currentPlanetIndex = event.planetIndex
                currentPlanetSelection = event.planet
            }

            FalconeSelectionScreenEvent.onPrev -> {
                sendUiEvent(FalconeScreenUIEvent.prevConfirmDialog(selectionStack.size))
            }

            FalconeSelectionScreenEvent.onPrevConfirm -> {
                if (selectionStack.isEmpty()) return
                sendUiEvent(
                    FalconeScreenUIEvent.refreshUI(
                        selectionStack.size,
                        Pair(peekSelection().planetIndex, peekSelection().vehicleIndex)
                    )
                )
                popSelection()
            }

            is FalconeSelectionScreenEvent.onVehicleSelection -> {
                currentVehicleSelection = event.vehicle
                currentVehicleIndex = event.vehicleIndex
            }
        }
    }

    private fun initToken() {
        viewModelScope.launch(Dispatchers.IO) {
            falconeNetworkDataSourceImpl.getToken()
        }
    }

    fun getPlanetsData() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = falconeNetworkDataSourceImpl.getPlanets()
            data.onSuccess {
                _planetsData.postValue(it)
            }
        }
    }

    fun getVehiclesData() {
        viewModelScope.launch(Dispatchers.IO) {
            val data = falconeNetworkDataSourceImpl.getVehicles()
            data.onSuccess {
                _vehiclesData.postValue(it)
            }
        }
    }


    private fun sendUiEvent(event: FalconeScreenUIEvent) {
        viewModelScope.launch {
            _uievents.send(event)
        }
    }

    private val selectionStack = Stack<FalconeSelectionState>()

    private fun pushSelection() {
        selectionStack.push(
            FalconeSelectionState(
                currentPlanetIndex, currentPlanetSelection,
                currentVehicleIndex, currentVehicleSelection
            )
        )
    }

    private fun popSelection() {
        if (selectionStack.isNotEmpty()) {
            selectionStack.pop()
        }
    }

    private fun peekSelection(): FalconeSelectionState {
        if (selectionStack.isNotEmpty()) {
            return selectionStack.peek()
        }
        return FalconeSelectionState()
    }


    private fun getFindRequestFromStack(): FalconeFindingData {
        return FalconeFindingData(
            selectionStack.map { it.planetName ?: "" },
            selectionStack.map { it.vehicleName ?: "" })
    }
}