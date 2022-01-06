package com.example.spaceinfo.ui.pictureOfADay

import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.spaceinfo.ui.MainActivity
import com.example.spaceinfo.ui.pictureOfADayFullscreen.FullScreenPictureFragment
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.R])
class PictureOfADayFragmentTest {

    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun close() {
        scenario.close()
    }

    @Test
    fun activity_AssertNotNull() {
        scenario.onActivity {
            assertNotNull(it)
        }
    }

    @Test
    fun activity_IsResumed() {
        assertEquals(Lifecycle.State.RESUMED, scenario.state)
    }

    @Test
    fun activity_PictureOfADayFragment_contract() {
        scenario.onActivity {
            assertTrue(it is PictureOfADayFragment.Contract)
        }
    }

    @Test
    fun activity_FullScreenPictureFragment_contract() {
        scenario.onActivity {
            assertTrue(it is FullScreenPictureFragment.Contract)
        }
    }
}