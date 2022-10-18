package com.will.kgu_math.fragments.subject

import androidx.lifecycle.ViewModel

class SubjectViewModel : ViewModel() {

    lateinit var subjectPath: String

    var themeNames: List<String>? = null
}