package com.example.falcone_finder.business.usecases.falcone_selection

import com.example.falcone_finder.business.domain.models.FalconeFindingData
import com.example.falcone_finder.business.domain.models.FalconeSelectionState
import java.util.Stack
import javax.inject.Inject

class FalconeDestinationStackUseCase @Inject constructor(

) {

    private val selectionStack = Stack<FalconeSelectionState>()
    fun pushSelection(
        currentPlanetIndex: Int,
        currentPlanetSelection: String?,
        currentVehicleIndex: Int,
        currentVehicleSelection: String?
    ) {
        selectionStack.push(
            FalconeSelectionState(
                currentPlanetIndex, currentPlanetSelection,
                currentVehicleIndex, currentVehicleSelection
            )
        )
    }

    fun popSelection() {
        if (selectionStack.isNotEmpty()) {
            selectionStack.pop()
        }
    }

    fun peekSelection(): FalconeSelectionState {
        if (selectionStack.isNotEmpty()) {
            return selectionStack.peek()
        }
        return FalconeSelectionState()
    }

    fun getFindRequestFromStack(): FalconeFindingData {
        return FalconeFindingData(
            selectionStack.map { it.planetName ?: "" },
            selectionStack.map { it.vehicleName ?: "" })
    }

    fun clearStack() {
        selectionStack.clear()
    }

    fun currentSize() = selectionStack.size

    fun isMaxLimitReached() = selectionStack.size == MAX_STACK_SIZE

    fun maxPlanets() = MAX_STACK_SIZE

    companion object {
        private const val MAX_STACK_SIZE = 4
    }
}