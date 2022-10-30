package com.will.kgu_math.fragments.subject

import androidx.lifecycle.ViewModel

class SubjectViewModel : ViewModel() {

    var subjectPath: String = ""

    lateinit var themeNames: List<String>
}