package com.will.kgu_math.fragments.subjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.will.kgu_math.ViewPagerAdapter
import com.will.kgu_math.databinding.FragmentSubjectsBinding
import com.will.kgu_math.fragments.subject.SubjectFragment
import com.will.kgu_math.app.LocaleManager

class SubjectsFragment : Fragment() {

    private val vm: SubjectsViewModel by viewModels()

    private var _binding: FragmentSubjectsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubjectsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.getSubjectNames()

        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        val fragments = vm.subjectNames
            .map { subjectName -> SubjectFragment("root/$subjectName") }


        binding.viewPager.adapter = ViewPagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
            fragments
        )
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = LocaleManager.getStringByName(vm.subjectNames[pos])
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}