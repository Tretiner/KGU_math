package com.will.kgu_math.fragments.theme

import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    lateinit var themePath: String

    var filePaths: List<String>? = null
}