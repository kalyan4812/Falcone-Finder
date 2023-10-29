package com.example.falcone_finder.business.domain.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class FalconeSelectionState(
    val planetIndex: Int=0,
    val planetName: String? = "",
    val vehicleIndex: Int = 0,
    val vehicleName: String? = ""
):Parcelable
