package com.example.spaceinfo.ui

import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

fun String.toFirstLettersColoredSpannable(color: Int): CharSequence = TextUtils.concat(*split(" ").map {
    SpannableString("$it ").apply {
        if (first().isLetter()) {
            setSpan(ForegroundColorSpan(color), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }
}.toTypedArray()).trim()

fun AppCompatActivity.hideSystemUi(view: View) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, view).let { controller ->
        controller.hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun AppCompatActivity.showSystemUi(view: View) {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(
        window,
        view
    ).show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
}