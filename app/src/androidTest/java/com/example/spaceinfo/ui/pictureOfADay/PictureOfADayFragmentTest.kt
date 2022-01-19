package com.example.spaceinfo.ui.pictureOfADay

import androidx.test.rule.ActivityTestRule
import com.example.spaceinfo.screens.PictureOfADayScreen
import com.example.spaceinfo.ui.MainActivity
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test

class PictureOfADayFragmentInstrumentalTest : TestCase() {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, false, true)

    @Test
    fun chips_test() = run {
        step("Initial chips state") {
            PictureOfADayScreen {
                todayChip {
                    isChecked()
                }
                yesterdayChip {
                    isNotChecked()
                }
                twoDaysBeforeChip {
                    isNotChecked()
                }
            }
        }

        step("click yesterday chip") {
            PictureOfADayScreen {
                yesterdayChip {
                    click()
                    isChecked()
                }
                todayChip {
                    isNotChecked()
                }
                twoDaysBeforeChip {
                    isNotChecked()
                }
            }
        }

        step("click two days before chip") {
            PictureOfADayScreen {
                twoDaysBeforeChip {
                    click()
                    isChecked()
                }
                todayChip {
                    isNotChecked()
                }
                yesterdayChip {
                    isNotChecked()
                }
            }
        }

        step("click today chip") {
            PictureOfADayScreen {
                todayChip {
                    click()
                    isChecked()
                }
                yesterdayChip {
                    isNotChecked()
                }
                twoDaysBeforeChip {
                    isNotChecked()
                }
            }
        }
    }
}