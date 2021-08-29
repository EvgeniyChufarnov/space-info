package com.example.spaceinfo.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.spaceinfo.R
import com.example.spaceinfo.databinding.ActivityMainBinding
import com.example.spaceinfo.ui.pictureOfADay.PictureOfADayFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.bottomAppBar)
        initBottomAppBar()

        navigateTo(PictureOfADayFragment())
    }

    private fun initBottomAppBar() {
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.navigationView)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.bottomAppBar.setNavigationOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            enableBackgroundOnNavigation()
        }

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            disableBackgroundOnNavigation()
            navigate(menuItem)
            true
        }

        binding.scrim.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            disableBackgroundOnNavigation()
        }
    }

    private fun enableBackgroundOnNavigation() {
        binding.scrim.isVisible = true
        binding.scrim.isClickable = true
        binding.scrim.isFocusable = true
    }

    private fun disableBackgroundOnNavigation() {
        binding.scrim.isVisible = false
        binding.scrim.isClickable = false
        binding.scrim.isFocusable = false
    }

    private fun navigate(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.picture_of_a_day_item -> {
                navigateTo(PictureOfADayFragment())
            }
            R.id.space_weather_item -> {

            }
        }
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_layout, fragment)
            .commit()
    }
}