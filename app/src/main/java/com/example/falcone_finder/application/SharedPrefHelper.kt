package com.example.falcone_finder.application

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPrefHelper @Inject constructor(private val pref: SharedPreferences) {
    companion object {
        private const val TOKEN = "token"
    }

    private var editor: SharedPreferences.Editor = pref.edit()

    var token: String?
        get() = pref.getString(TOKEN, null)
        set(v) {
            editor.putString(TOKEN, v)
            editor.commit()
        }
}