package com.will.kgu_math.fragments.pdf_viewer

import android.graphics.Bitmap
import android.printservice.PrinterDiscoverySession
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.will.kgu_math.databinding.TemplatePageBinding
import kotlinx.coroutines.*
import java.util.concurrent.Semaphore

class PagesAdapter : RecyclerView.Adapter<PagesAdapter.ViewHolder>() {
    private val cScope = CoroutineScope(Dispatchers.IO)

    private var pages: PdfList = PdfList()

    private val renderLock: Any = Any()

    public fun updateData(pdfList: PdfList) {
        pages = pdfList
    }

    inner class ViewHolder(private val binding: TemplatePageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pageBitmap: Bitmap) {
            binding.root.setImageBitmap(pageBitmap)
        }
    }

    override fun onBindViewHolder(holder: PagesAdapter.ViewHolder, pos: Int) {
        holder.bind(pages[pos].bitmap)

        if (!pages[pos].isRendered) {
            cScope.launch {
                synchronized(renderLock) {
                    holder.bind(pages.render(pos))
                }
                pages[pos].isRendered = true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        TemplatePageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount(): Int = pages.size
}