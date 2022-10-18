package com.will.kgu_math

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fm: FragmentManager,
    lc: Lifecycle,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fm, lc) {

    override fun createFragment(pos: Int) = fragments[pos]

    override fun getItemCount(): Int = fragments.size
}