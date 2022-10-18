package com.will.kgu_math.fragments.pdf_viewer

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.will.kgu_math.databinding.TemplatePageBinding

class PagesAdapter(
    private val pages: List<Bitmap>
) : RecyclerView.Adapter<PagesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: TemplatePageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pageBitmap: Bitmap) {
            binding.root.setImageBitmap(pageBitmap)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagesAdapter.ViewHolder {
        val binding = TemplatePageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagesAdapter.ViewHolder, pos: Int) =
        holder.bind(pages[pos])

    override fun getItemCount(): Int = pages.size
}