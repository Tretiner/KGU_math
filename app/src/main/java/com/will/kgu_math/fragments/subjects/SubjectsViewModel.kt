package com.will.kgu_math.fragments.subjects

import androidx.lifecycle.ViewModel
import com.will.kgu_math.app.AssetsManager

class SubjectsViewModel : ViewModel() {

    var semestr: String? = null
    private var _subjectNames: MutableList<String>? = null
    val subjectNames get() = _subjectNames as List<String>

    fun getSubjectNames(){
        if (_subjectNames != null) return

        _subjectNames = AssetsManager.mapAssets("root") as MutableList
    }
}
