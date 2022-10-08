package com.will.kgu_math.fragments

import android.net.Uri
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    public var firstName = ""
    public var secondName = ""

    public var fileLoaded = false
    public var fileName = ""
    public var filePath = ""

    fun loadAndParseFile(uri: Uri) {

        fileLoaded = true
    }
    // TODO: Implement the ViewModel
}