package com.will.kgu_math.utils

import com.will.kgu_math.App

object AssetsManager {

    /**
     * Uses assets.list(path) and maps through it
     *
     * dirPath - path to directory
     *
     * onIterated - called on every map iteration
     */
    fun mapAssets(
        dirPath: String,
        onIterated: ((String) -> String)? = null
    ): List<String> {
        val assets = App.context.assets.list(dirPath) ?: throw Exception("Directory $dirPath is not found")

        return onIterated?.let { assets.map(it) } ?: assets.toList()
    }


}