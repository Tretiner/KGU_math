package com.will.kgu_math.fragments.pdf_viewer

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.graphics.pdf.PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.will.kgu_math.App
import com.will.kgu_math.databinding.FragmentPdfViewerBinding
import com.will.kgu_math.decorators.Spacer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PdfViewerFragment(private val filePath: String? = null) : Fragment() {

    private val vm: PdfViewerViewModel by viewModels()

    private var _binding: FragmentPdfViewerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfViewerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            if (filePath != null && filePath != vm.filePath) {
                vm.filePath = filePath
                renderPdf()
            }

            withContext(Dispatchers.Main) {
                binding.rwPages.apply {
                    addItemDecoration(Spacer(20))
                    adapter = PagesAdapter(vm.pages)
                }
            }
        }

    }

    private fun makeBitmapForPdfPage(page: PdfRenderer.Page) = Bitmap.createBitmap(
        resources.displayMetrics.densityDpi * page.width / 100,
        resources.displayMetrics.densityDpi * page.height / 100,
        Bitmap.Config.ARGB_8888
    )

    private fun renderPdf(){
        vm.pages.clear()

        val file = File(App.context.cacheDir, "temp.pdf")

        App.context.assets.open(vm.filePath!!).use { inp ->
            file.outputStream().use { out ->
                inp.copyTo(out)
            }
        }

        val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)

        val pdfRenderer = PdfRenderer(pfd)

        pdfRenderer.renderPages(
            onCreateBitmap = { page -> makeBitmapForPdfPage(page) },
            onRender = { bitmap -> vm.pages.add(bitmap) },
            onComplete = {  }
        )
    }

    private fun PdfRenderer.renderPages(onCreateBitmap: (PdfRenderer.Page) -> Bitmap, onRender: (Bitmap) -> Unit, onComplete: () -> Unit) {
        for (i in 0 until pageCount) {
            openPage(i).use { page ->
                val bitmap = onCreateBitmap(page)

                page.render(bitmap, null, null, RENDER_MODE_FOR_DISPLAY)

                onRender(bitmap)
            }
        }
        close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}