package com.will.kgu_math.fragments.pdf_viewer

import android.graphics.Bitmap


data class Page(
    var bitmap: Bitmap,
    var isRendered: Boolean = false
)
