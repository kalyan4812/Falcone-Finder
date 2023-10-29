package com.example.falcone_finder.business.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class FindApiRequest(
    val token: String?="",
    @SerializedName("planet_names") val planetNames: List<String>?,
    @SerializedName("vehicle_names") val VehicleNames: List<String>?
)