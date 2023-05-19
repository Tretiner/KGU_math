package com.will.kgu_math.fragments.subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.will.kgu_math.R
import com.will.kgu_math.databinding.FragmentSubjectBinding
import com.will.kgu_math.fragments.theme.ThemeFragment
import com.will.kgu_math.app.AssetsManager
import com.will.kgu_math.fragments.pdf_viewer.PdfViewerFragment

class SubjectFragment(private val subjectPath: String? = null) : Fragment() {

    private val vm: SubjectViewModel by viewModels()

    private var _binding: FragmentSubjectBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVariants.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                addToBackStack("v")
                replace(R.id.nav_host_fragment, PdfViewerFragment("${vm.subjectPath}/v.pdf"), "v")
            }
        }

        subjectPath?.let {
            vm.subjectPath = subjectPath

            vm.themeNames = AssetsManager.mapAssets(vm.subjectPath)
                .filter{ subjectName -> subjectName[0].isDigit() }
        }

        binding.rwThemes.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(binding.rwThemes.context, DividerItemDecoration.VERTICAL))
            adapter = ThemesAdapter(
                vm.themeNames,
                onSelected = ::showTheme
            )
        }
    }

    private fun showTheme(themeName: String) {
        requireActivity().supportFragmentManager.commit {
            setReorderingAllowed(true)
            addToBackStack(themeName)
            replace(R.id.nav_host_fragment, ThemeFragment("${vm.subjectPath}/$themeName"), themeName)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rwThemes.adapter = null
        _binding = null
    }
}