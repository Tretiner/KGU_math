package com.will.kgu_math

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Resources

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        res = resources
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        lateinit var res: Resources
    }
}