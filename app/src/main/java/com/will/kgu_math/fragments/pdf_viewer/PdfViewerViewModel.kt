package com.will.kgu_math.fragments.pdf_viewer

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel

class PdfViewerViewModel : ViewModel() {
    var filePath: String? = null

    val pages = mutableListOf<Bitmap>()
}