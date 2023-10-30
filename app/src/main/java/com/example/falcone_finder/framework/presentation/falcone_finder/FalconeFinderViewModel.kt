package com.example.falcone_finder.framework.presentation.falcone_finder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.falcone_finder.business.data.network.implementation.FalconeNetworkDataSourceImpl
import com.example.falcone_finder.business.domain.models.FalconeFindingData
import com.example.falcone_finder.business.domain.models.FindResponse
import com.example.falcone_finder.business.usecases.falcone_finding.FindPrincessUseCase
import com.example.falcone_finder.framework.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FalconeFinderViewModel @Inject constructor(
    private val findPrincessUseCase: FindPrincessUseCase,
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {


    private val _statusData: MutableLiveData<FindResponse> = MutableLiveData()
    val statusData = _statusData

    init {
        makeFindFalconeApiCall()
    }

    private fun makeFindFalconeApiCall() {
        val data: FalconeFindingData? = savedStateHandle[Constants.FalconeFindingData]
        data?.let {
            viewModelScope.launch {
                findPrincessUseCase.invoke(it.planetNames, it.vehicleNames).onSuccess { response ->
                    _statusData.postValue(response)
                }
            }
        }
    }
}