package com.example.falcone_finder.business.domain.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName


@Keep
data class FindApiResponse(
    @SerializedName("planet_name") val planetName: String?="",
    val status: String?="",
    val error: String?=""
)