package com.example.falcone_finder.framework.presentation.falcone_selection

import com.example.falcone_finder.business.domain.models.FalconeFindingData

sealed class FalconeScreenUIEvent {
    data class refreshUI(
        val indexOfSelection: Int,
        val prevState: Pair<Int, Int>? = null,
        val isFinalStage: Boolean = false
    ) :
        FalconeScreenUIEvent()

    data class navigateToFindPrincess(val data: FalconeFindingData) : FalconeScreenUIEvent()

    data class prevConfirmDialog(val index: Int) : FalconeScreenUIEvent()


}
