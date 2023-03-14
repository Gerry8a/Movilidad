package com.bancomer.bbva.bbvamovilidad.utils

import android.content.SharedPreferences
import android.util.Log
import javax.inject.Inject

class Preferences @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    fun save(key: String, value: Any) {
        val editor = sharedPreferences.edit()
        when (value) {
            is Int -> editor.putInt(key, value)
            is String -> editor.putString(key, value.toString())
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
        }
        editor.apply()
    }

    fun get(key: String, defaultValue: Any): Any? {
        try {
            when (defaultValue) {
                is String -> return sharedPreferences.getString(key, defaultValue.toString())
                is Int -> return sharedPreferences.getInt(key, defaultValue)
                is Boolean -> return sharedPreferences.getBoolean(key, defaultValue)
                is Float -> return sharedPreferences.getFloat(key, defaultValue)
            }
        } catch (e: Exception) {
            Log.e("Exception", e.message!!)
            return defaultValue;
        }
        return defaultValue
    }

    fun remove(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun removeAll() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}