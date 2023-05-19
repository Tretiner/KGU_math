package com.will.kgu_math.fragments.services;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment;
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.will.kgu_math.ViewPagerAdapter
import com.will.kgu_math.databinding.FragmentServicesBinding
import com.will.kgu_math.fragments.settings.SettingsFragment
import com.will.kgu_math.fragments.subjects.SubjectsFragment
import com.will.kgu_math.app.LocaleManager

class ServicesFragment() : Fragment() {

    private val vm: ServicesViewModel by viewModels()

    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServicesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
    }

    private fun setupComponents() {
        setupViewPager()
        setupTabLayout()
    }

    private fun setupViewPager() {
        binding.viewPager.isUserInputEnabled = false

        val fragments = listOf(
            SubjectsFragment(),
            SettingsFragment()
        )

        binding.viewPager.adapter = ViewPagerAdapter(
            childFragmentManager,
            viewLifecycleOwner.lifecycle,
            fragments
        )
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = LocaleManager.getStringByName(vm.fragmentNames[pos])
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}