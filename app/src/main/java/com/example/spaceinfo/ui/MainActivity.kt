package com.example.spaceinfo.ui

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.spaceinfo.R
import com.example.spaceinfo.databinding.ActivityMainBinding
import com.example.spaceinfo.ui.pictureOfADay.PictureOfADayFragment
import com.example.spaceinfo.ui.pictureOfADayFullscreen.FullScreenPictureFragment
import com.example.spaceinfo.ui.picturesFromMarsContainer.PicturesFromMarsContainerFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

private const val NIGHT_THEME_PREFERENCES_KEY = "night theme preferences"
private const val NIGHT_THEME_KEY = "night theme"

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main), PopupMenu.OnMenuItemClickListener,
    PictureOfADayFragment.Contract, FullScreenPictureFragment.Contract {

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private var isDarkTheme: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isDarkTheme = isDarkTheme()
        applyTheme()

        setSupportActionBar(binding.bottomAppBar)
        initBottomAppBar()

        navigateTo(PictureOfADayFragment())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
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
            R.id.mars_pictures_item -> {
                navigateTo(PicturesFromMarsContainerFragment())
            }
        }
    }

    private fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_layout, fragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.settings_item -> {
            showSettingsMenu()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showSettingsMenu() {
        PopupMenu(this, binding.bottomAppBar, Gravity.END).apply {
            setOnMenuItemClickListener(this@MainActivity)
            inflate(R.menu.menu_settings_options)
            menu.findItem(R.id.dark_theme_item).isChecked = isDarkTheme
            show()
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dark_theme_item -> {
                changeTheme()
                true
            }
            else -> false
        }
    }

    private fun changeTheme() {
        isDarkTheme = !isDarkTheme

        applyTheme()
        saveThemeToPreferences()
    }

    private fun isDarkTheme(): Boolean {
        val sharedPref = getSharedPreferences(NIGHT_THEME_PREFERENCES_KEY, MODE_PRIVATE)
        return sharedPref.getBoolean(NIGHT_THEME_KEY, false)
    }

    private fun saveThemeToPreferences() {
        val sharedPref = getSharedPreferences(NIGHT_THEME_PREFERENCES_KEY, MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(NIGHT_THEME_KEY, isDarkTheme)
        editor.apply()
    }

    private fun applyTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun showPictureFullScreen(path: String, sharedView: View, sharedViewName: String) {
        hideSystemUi(binding.mainContainer)
        binding.bottomAppBar.performHide()

        supportFragmentManager.beginTransaction()
            .setReorderingAllowed(true)
            .addSharedElement(sharedView, sharedViewName)
            .replace(
                binding.containerLayout.id,
                FullScreenPictureFragment.getInstance(path)
            )
            .addToBackStack(null)
            .commit()
    }

    override fun exitFullScreenState() {
        showSystemUi(binding.mainContainer)
        binding.bottomAppBar.performShow()
    }

    override fun closeFullScreenFragment() {
        supportFragmentManager.popBackStack()
    }
}