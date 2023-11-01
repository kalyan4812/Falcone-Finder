package com.example.falcone_finder.business.domain.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class Vehicle(
    val name: String,
    val amount: Int,
    val maxDistance: Int,
    val speed: Int
) : Parcelable {
}