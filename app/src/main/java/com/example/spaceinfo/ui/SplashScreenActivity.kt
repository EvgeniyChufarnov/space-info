package com.example.spaceinfo.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.spaceinfo.R
import com.example.spaceinfo.databinding.ActivitySplashScreenBinding

private const val splash_timeout = 2000L

class SplashScreenActivity : AppCompatActivity(R.layout.activity_splash_screen) {
    private val binding: ActivitySplashScreenBinding by viewBinding(ActivitySplashScreenBinding::bind)

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemUI(binding.splashScreen)

        if (savedInstanceState == null) {
            handler.postDelayed(splash_timeout) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}