package com.example.spaceinfo.ui.picturesFromMarsContainer

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PicturesFromMarsViewPagerAdapter(
    fragment: Fragment,
    private val rovers: List<String>,
    private val builder: (String) -> Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = rovers.size

    override fun createFragment(position: Int) = builder(rovers[position])
}