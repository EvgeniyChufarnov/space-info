package com.example.spaceinfo.automator

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.example.spaceinfo.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SdkSuppress(minSdkVersion = 18)
class BehaviorTest {

    private val uiDevice: UiDevice = UiDevice.getInstance(getInstrumentation())
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val packageName = context.packageName

    @Before
    fun setup() {
        uiDevice.pressHome()

        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)

        uiDevice.wait(Until.hasObject(By.pkg(packageName).depth(0)), TIMEOUT)
    }

    @Test
    fun test_open_fullscreen_image() {
        val pictureOfADayImageView = uiDevice.wait(
            Until.findObject(By.res(packageName, "picture_of_a_day_image_view")),
            TIMEOUT
        )

        pictureOfADayImageView.click()

        onView(withId(R.id.fullscreen_container)).check(matches(isDisplayed()))
    }

    @Test
    fun test_close_fullscreen_image() {
        val pictureOfADayImageView = uiDevice.wait(
            Until.findObject(By.res(packageName, "picture_of_a_day_image_view")),
            TIMEOUT
        )

        pictureOfADayImageView.click()

        val fullScreenImageView = uiDevice.wait(
            Until.findObject(By.res(packageName, "full_screen_picture_image_view")),
            TIMEOUT
        )

        fullScreenImageView.click()

        onView(withId(R.id.fullscreen_container)).check(doesNotExist())
    }

    companion object {
        private const val TIMEOUT = 5000L
    }
}

