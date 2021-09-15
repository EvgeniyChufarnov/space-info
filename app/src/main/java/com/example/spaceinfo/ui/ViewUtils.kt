package com.example.spaceinfo.ui

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

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