package com.will.kgu_math.fragments.subject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.will.kgu_math.App
import com.will.kgu_math.R
import com.will.kgu_math.databinding.FragmentSubjectBinding
import com.will.kgu_math.fragments.theme.ThemeFragment

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

        subjectPath?.let { vm.subjectPath = it }

        if (vm.themeNames == null) {
            vm.themeNames = App.context.assets
                .list(vm.subjectPath)!!
                .toList()
        }

        binding.rwThemes.apply {
            adapter = ThemesAdapter(
                vm.themeNames!!,
                onSelected = { showTheme(it) }
            )
        }
    }

    private fun showTheme(themeName: String) {
        requireActivity().supportFragmentManager.commit {
            add(R.id.nav_host_fragment, ThemeFragment("$subjectPath/$themeName"))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}