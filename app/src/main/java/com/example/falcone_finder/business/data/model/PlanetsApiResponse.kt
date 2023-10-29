package com.example.falcone_finder.business.data.model

import androidx.annotation.Keep

@Keep
data class PlanetsApiResponse(
    val name: String,
    val distance: Int
)