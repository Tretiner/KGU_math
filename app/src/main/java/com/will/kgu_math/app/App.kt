package com.will.kgu_math.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import com.will.kgu_math.preferences.SettingsPreferences

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        res = resources
        config = resources.configuration

        setUiMode()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        lateinit var res: Resources

        lateinit var config: Configuration
    }

    private fun setUiMode(){
        if (SettingsPreferences.uiMode != MODE_NIGHT_FOLLOW_SYSTEM)
            setDefaultNightMode(SettingsPreferences.uiMode)
    }
}