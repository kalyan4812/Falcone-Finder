package com.example.findingfalcone.domain.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class Planet(
    val name: String,
    val distance: Int
) : Parcelable