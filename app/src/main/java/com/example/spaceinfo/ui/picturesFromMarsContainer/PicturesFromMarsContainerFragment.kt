package com.example.spaceinfo.ui.picturesFromMarsContainer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.spaceinfo.R
import com.example.spaceinfo.ROVERS_NAMES
import com.example.spaceinfo.databinding.FragmentPicturesFromMarsContainerBinding
import com.example.spaceinfo.ui.picturesFromMars.PicturesFromMarsFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PicturesFromMarsContainerFragment : Fragment(R.layout.fragment_pictures_from_mars_container) {
    private val binding: FragmentPicturesFromMarsContainerBinding by viewBinding(
        FragmentPicturesFromMarsContainerBinding::bind
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.marsPicturesViewPager.adapter = PicturesFromMarsViewPagerAdapter(
            this,
            ROVERS_NAMES
        ) {
            PicturesFromMarsFragment.getInstance(it)
        }

        TabLayoutMediator(binding.tabLayout, binding.marsPicturesViewPager) { tab, position ->
            tab.text = ROVERS_NAMES[position]
        }.attach()
    }
}