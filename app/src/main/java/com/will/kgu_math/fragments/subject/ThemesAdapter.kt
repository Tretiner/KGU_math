package com.will.kgu_math.fragments.subject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.will.kgu_math.databinding.TemplateThemeBinding
import com.will.kgu_math.app.LocaleManager

class ThemesAdapter(
    items: List<String>,
    private val onSelected: (String) -> Unit
) : RecyclerView.Adapter<ThemesAdapter.ViewHolder>() {

    private val _items = items

    inner class ViewHolder(private val binding: TemplateThemeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(themeName: String) {
            binding.apply {
                name.text = LocaleManager.getStringByName(themeName.substringAfter('_'))
            }
        }

        fun setListeners(): ViewHolder {
            binding.apply {
                root.setOnClickListener { onSelected(_items[adapterPosition]) }
            }

            return this
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemesAdapter.ViewHolder {
        val binding = TemplateThemeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
            .setListeners()
    }

    override fun onBindViewHolder(holder: ThemesAdapter.ViewHolder, pos: Int) =
        holder.bind(_items[pos])

    override fun getItemCount(): Int = _items.size
}