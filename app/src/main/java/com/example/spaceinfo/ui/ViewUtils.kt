package com.example.spaceinfo.ui

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun String.toFirstLettersColoredSpannable(color: Int): Spannable {
    val spannable = SpannableStringBuilder(this)
    val chars = toCharArray()

    for (i in indices) {
        if (i == 0 || chars[i - 1] == ' ' && chars[i].isLetter()) {
            spannable.setSpan(ForegroundColorSpan(color), i, i + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }

    return spannable
}

fun AppCompatActivity.hideSystemUI(view: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, view).let { controller ->
        controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun AppCompatActivity.showSystemUI(view: View) {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        view
    ).show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
}