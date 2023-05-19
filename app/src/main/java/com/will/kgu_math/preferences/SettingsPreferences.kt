package com.will.kgu_math.preferences

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.will.kgu_math.app.App
import com.will.kgu_math.app.LocaleManager

object SettingsPreferences {
    val root: SharedPreferences = App.context.getSharedPreferences("SETTINGS", Application.MODE_PRIVATE)

    var uiMode: Int
        get() = root.getInt("UI_MODE", AppCompatDelegate.MODE_NIGHT_NO)
        set(new) = root.edit { putInt("UI_MODE", new) }

    var uiModeName: String
        get() = root.getString("UI_MODE_NAME", LocaleManager.getStringByName("mode_night_no"))!!
        set(new) = root.edit { putString("UI_MODE_NAME", new) }

}