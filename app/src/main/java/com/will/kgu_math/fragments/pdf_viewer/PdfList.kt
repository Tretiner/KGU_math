package com.will.kgu_math.fragments.pdf_viewer

import android.graphics.*
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import com.will.kgu_math.app.App
import java.io.File
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class PdfList(
    private val pfd: ParcelFileDescriptor? = null,
    private val isNightMode: Boolean = false
) {
    private lateinit var pdfRenderer: PdfRenderer

    var size: Int = 0
        private set

    lateinit var pages: Array<Page>

    init {
        if (pfd != null) {
            pdfRenderer = PdfRenderer(pfd)
            size = pdfRenderer.pageCount
            pages = Array(pdfRenderer.pageCount) { Page(pickPage(it)) }
        }
    }

    companion object {
        public fun new(filePath: String, isNightMode: Boolean): PdfList {
            val file = File(App.context.cacheDir, "temp${Random.nextInt()}.pdf")
            file.deleteOnExit()

            App.context.assets.open(filePath).use { inp ->
                file.outputStream().use { out ->
                    inp.copyTo(out)
                }
            }

            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            return PdfList(pfd, isNightMode)
        }
    }

    operator fun get(pos: Int): Page = pages[pos]

    fun render(index: Int): Bitmap {
        val millis = measureTimeMillis {
            pdfRenderer.renderPage(index)
        }
        println("render time: $millis ms")
        return pages[index].bitmap
    }

    private fun pickPage(index: Int): Bitmap = pdfRenderer.openPage(index).use { it.toBitmap() }

    private fun PdfRenderer.renderPage(index: Int) = openPage(index).use { page ->
        var bitmap = pages[index].bitmap

        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        if (isNightMode)
            bitmap = invert(bitmap)

        pages[index].bitmap = bitmap

        bitmap
    }

    private fun PdfRenderer.Page.toBitmap() = Bitmap.createBitmap(
        App.res.displayMetrics.densityDpi * this.width / 160,
        App.res.displayMetrics.densityDpi * this.height / 160,
        Bitmap.Config.ARGB_8888
    )

    private fun invert(src: Bitmap): Bitmap {
        val bitmap = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()

        val matrixGrayscale = ColorMatrix().apply { setSaturation(0f) }
        val matrixInvert = ColorMatrix().apply {
            set(
                floatArrayOf(
                    -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                    0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                    0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                    0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                )
            )
            preConcat(matrixGrayscale)
        }

        paint.colorFilter = ColorMatrixColorFilter(matrixInvert)
        canvas.drawBitmap(src, 0f, 0f, paint)

        return bitmap
    }

    fun clear() {
        pages.forEach { it.bitmap.recycle() }
    }
}