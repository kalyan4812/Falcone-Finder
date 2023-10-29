package com.example.falcone_finder.business.domain.models

sealed class FindResponse() {
    data class Success(val planetName: String) : FindResponse()
    object Failure : FindResponse()
}