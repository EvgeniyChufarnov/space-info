package com.example.spaceinfo.ui.picturesFromMars

import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spaceinfo.R
import com.example.spaceinfo.ROVERS_NAMES
import com.geekbrains.tests.TIMEOUT
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PicturesFromMarsFragmentTest {

    private lateinit var scenario: FragmentScenario<PicturesFromMarsFragment>

    @Before
    fun setup() {
        val fragmentArgs = bundleOf("rover_name" to ROVERS_NAMES.first())
        scenario = launchFragmentInContainer(fragmentArgs)
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun fragment_AssertNotNull() {
        scenario.onFragment {

            assertNotNull(it)
        }
    }

    @Test
    fun fragment_ScrollToPosition() {
        onView(isRoot()).perform(waitFor(TIMEOUT))

        onView(withId(R.id.pictures_from_mars_recycler_view))
            .perform(
                scrollToPosition<PicturesFromMarsAdapter.PictureFromMarsViewHolder>(5),
            )
    }

    private fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): org.hamcrest.Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for " + delay + "milliseconds"
            }

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }
        }
    }
}