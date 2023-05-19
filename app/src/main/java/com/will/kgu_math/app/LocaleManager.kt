package com.will.kgu_math.app

object LocaleManager {
    fun getStringByName(name: String): String {
        val stringId = App.res.getIdentifier(name.replace('-', '_'), "string", App.context.packageName)

        if(stringId == 0)
            return name

        return try {
            App.res.getString(stringId)
        } catch (e: Exception){
            println(e)
            name
        }
    }
}