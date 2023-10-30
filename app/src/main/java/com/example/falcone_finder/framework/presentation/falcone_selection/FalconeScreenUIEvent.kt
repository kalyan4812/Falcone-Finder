package com.example.falcone_finder.framework.presentation.falcone_selection

import com.example.falcone_finder.business.domain.models.FalconeFindingData

sealed interface FalconeScreenUIEvent {
    data class refreshUI(
        val indexOfSelection: Int,
        val prevState: Pair<Int, Int>? = null,
        val isFinalStage: Boolean = false
    ) :
        FalconeScreenUIEvent

    data class navigateToFindPrincess(val data: FalconeFindingData) : FalconeScreenUIEvent

    data class prevConfirmDialog(val index: Int) : FalconeScreenUIEvent

    data class progressBarStatus(
        val show: Boolean = false
    ) : FalconeScreenUIEvent

    data class showTost(
        val content: String? = ""
    ) : FalconeScreenUIEvent
}
