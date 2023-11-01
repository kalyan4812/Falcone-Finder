package com.example.falcone_finder.business.domain.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class FalconeFindingData(
    val planetNames: List<String>?, val vehicleNames: List<String>?
) : Parcelable
