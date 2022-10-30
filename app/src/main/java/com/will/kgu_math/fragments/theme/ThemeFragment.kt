package com.will.kgu_math.fragments.theme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.will.kgu_math.ViewPagerAdapter
import com.will.kgu_math.databinding.FragmentThemeBinding
import com.will.kgu_math.fragments.pdf_viewer.PdfViewerFragment
import com.will.kgu_math.utils.AssetsManager

class ThemeFragment(private val themePath: String? = null) : Fragment() {

    private val vm: ThemeViewModel by viewModels()

    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themePath?.let {
            vm.themePath = it

            vm.filePaths = AssetsManager.mapAssets(vm.themePath) { fileName ->  "${vm.themePath}/$fileName"}
        }

        setupViewPager()
    }

    private fun setupViewPager() {
        val fragments = vm.filePaths!!.map { filePath -> PdfViewerFragment(filePath) }

        binding.viewPager.adapter = ViewPagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
            fragments
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}