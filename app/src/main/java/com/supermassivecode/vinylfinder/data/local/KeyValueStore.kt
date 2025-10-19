package com.supermassivecode.vinylfinder.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.json.Json

class KeyValueStore(context: Context) {

    val prefs: SharedPreferences = context.getSharedPreferences(
        "vinyl_finder_prefs",
        Context.MODE_PRIVATE
    )

    val json = Json { ignoreUnknownKeys = true }

    inline fun <reified T> store(key: String, value: T) {
        prefs.edit {
            when (value) {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is Boolean -> putBoolean(key, value)
                else -> putString(key, json.encodeToString(value))
            }
        }
    }

    inline fun <reified T> read(key: String): T? {
        return when (T::class) {
            String::class -> prefs.getString(key, null) as? T
            Int::class -> if (prefs.contains(key)) prefs.getInt(key, 0) as T else null
            Long::class -> if (prefs.contains(key)) prefs.getLong(key, 0L) as T else null
            Float::class -> if (prefs.contains(key)) prefs.getFloat(key, 0f) as T else null
            Boolean::class -> if (prefs.contains(key)) prefs.getBoolean(key, false) as T else null
            else -> prefs.getString(key, null)?.let { json.decodeFromString<T>(it) }
        } as T
    }

    fun remove(key: String) {
        prefs.edit { remove(key) }
    }

    fun clear() {
        prefs.edit { clear() }
    }

    companion object Keys {
        const val LAST_RECORD_PRICE_CHECK = "LAST_RECORD_PRICE_CHECK"
    }
}