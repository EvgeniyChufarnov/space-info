package com.example.spaceinfo.screens

import com.example.spaceinfo.R
import com.example.spaceinfo.ui.MainActivity
import com.example.spaceinfo.ui.pictureOfADay.PictureOfADayFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.bottomnav.KBottomNavigationView
import io.github.kakaocup.kakao.check.KCheckBox
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.toolbar.KToolbar

object PictureOfADayScreen : KScreen<PictureOfADayScreen>() {

    override val layoutId: Int = R.layout.fragment_picture_of_a_day
    override val viewClass: Class<*> = PictureOfADayFragment::class.java

    val todayChip = KCheckBox { withId(R.id.today_chip)}
    val yesterdayChip = KCheckBox { withId(R.id.yesterday_chip)}
    val twoDaysBeforeChip = KCheckBox { withId(R.id.two_days_before_chip)}
}