package com.will.kgu_math.app

object AssetsManager {
    fun mapAssets(
        dirPath: String,
        onIterated: ((String) -> String)? = null
    ): List<String> {
        val assets = App.context.assets.list(dirPath) ?: throw Exception("Directory $dirPath is not found")
        print(assets.joinToString(", "))

        return onIterated?.let { assets.map(it) } ?: assets.toList()
    }
}