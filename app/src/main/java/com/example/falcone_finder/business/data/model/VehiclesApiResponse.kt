package com.example.falcone_finder.business.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class VehiclesApiResponse(
    val name: String,
    @SerializedName("total_no") val amount: Int,
    @SerializedName("max_distance") val maxDistance: Int,
    val speed: Int
)