package com.will.kgu_math.fragments.pdf_viewer

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.will.kgu_math.R
import com.will.kgu_math.databinding.FragmentPdfViewerBinding
import com.will.kgu_math.decorators.Spacer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PdfViewerFragment(private val filePath: String? = null) : Fragment() {

    private val vm: PdfViewerViewModel by viewModels()

    private var _binding: FragmentPdfViewerBinding? = null
    private val binding get() = _binding!!

    private lateinit var pagesAdapter: PagesAdapter

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfViewerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (filePath == null && vm.filePath == null){
            Snackbar.make(requireView(), "Путь не указан идите нафиг", Snackbar.LENGTH_SHORT).show()
            return
        }

        ioScope.launch {
            if (vm.filePath != filePath){
                vm.filePath = filePath
                val isNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
                vm.pages = PdfList.new(vm.filePath!!, isNightMode)
            }

            pagesAdapter = PagesAdapter()
            pagesAdapter.updateData(vm.pages)

            withContext(Dispatchers.Main) {
                binding.rwPages.apply {
                    setHasFixedSize(true)
                    addItemDecoration(Spacer(10))
                    adapter = pagesAdapter
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rwPages.adapter = null
        _binding = null
    }
}