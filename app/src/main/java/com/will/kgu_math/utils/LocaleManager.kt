package com.will.kgu_math.utils

import com.will.kgu_math.App

object LocaleManager {

    fun getStringByName(name: String): String {
        val stringId = App.res.getIdentifier(name.replace('-', '_'), "string", App.context.packageName)

        if (stringId == 0)
            return name

        return App.res.getString(stringId)
    }
}