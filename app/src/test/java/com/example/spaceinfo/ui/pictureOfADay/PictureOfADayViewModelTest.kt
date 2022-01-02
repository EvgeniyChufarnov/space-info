package com.example.spaceinfo.ui.pictureOfADay

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.spaceinfo.domain.data.ErrorResponse
import com.example.spaceinfo.domain.data.ResultWrapper
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity
import com.example.spaceinfo.domain.repository.SpaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.assertEquals
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class PictureOfADayViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()
    private val scope = TestScope(dispatcher)

    private lateinit var viewModel: PictureOfADayViewModel

    private val successPicture = PictureOfADayEntity("", "", "", "", "", "image")
    private val successVideo = PictureOfADayEntity("", "", "", "", "", "video")

    @Mock
    var mockSpaceRepository: SpaceRepository = mock(SpaceRepository::class.java)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun pictureOfADayViewModel_init_loading() = scope.runTest {
        `when`(mockSpaceRepository.getPictureOfADay(any())).thenReturn(ResultWrapper.Success(
            successPicture
        ))

        viewModel = PictureOfADayViewModel(mockSpaceRepository)

        assertEquals(successPicture, viewModel.picture.value)
    }

    @Test
    fun pictureOfADayViewModel_state_show_video() = scope.runTest  {
        `when`(mockSpaceRepository.getPictureOfADay(any())).thenReturn(ResultWrapper.Success(
            successVideo
        ))

        viewModel = PictureOfADayViewModel(mockSpaceRepository)

        assertEquals(PictureOfADayScreenState.SHOW_VIDEO, viewModel.state.value)
    }

    @Test
    fun pictureOfADayViewModel_state_show_picture() = scope.runTest {
        `when`(mockSpaceRepository.getPictureOfADay(any())).thenReturn(ResultWrapper.Success(
            successPicture
        ))

        viewModel = PictureOfADayViewModel(mockSpaceRepository)
        viewModel.onPictureReady()

        assertEquals(PictureOfADayScreenState.SHOW_PICTURE, viewModel.state.value)
    }

    @Test
    fun pictureOfADayViewModel_state_network_error() = scope.runTest {
        `when`(mockSpaceRepository.getPictureOfADay(any())).thenReturn(ResultWrapper.NetworkError)

        viewModel = PictureOfADayViewModel(mockSpaceRepository)

        assertEquals(PictureOfADayScreenState.ERROR, viewModel.state.value)
    }

    @Test
    fun pictureOfADayViewModel_state_generic_error() = scope.runTest {
        `when`(mockSpaceRepository.getPictureOfADay(any())).thenReturn(ResultWrapper.GenericError(
            error = ErrorResponse("TestMessage")
        ))

        viewModel = PictureOfADayViewModel(mockSpaceRepository)

        assertEquals(PictureOfADayScreenState.ERROR, viewModel.state.value)
        assertEquals("TestMessage", viewModel.errorMessage.value)
    }

    @Test
    fun pictureOfADayViewModel_on_share_clicked() = scope.runTest  {
        `when`(mockSpaceRepository.getPictureOfADay(any())).thenReturn(ResultWrapper.Success(
            successPicture
        ))

        viewModel = PictureOfADayViewModel(mockSpaceRepository)

        viewModel.onShareClicked()

        assertEquals(successPicture, viewModel.shareEvent.value)
    }

    @Test
    fun pictureOfADayViewModel_on_share_clicked_error() = scope.runTest  {
        `when`(mockSpaceRepository.getPictureOfADay(any())).thenReturn(ResultWrapper.NetworkError)

        viewModel = PictureOfADayViewModel(mockSpaceRepository)

        viewModel.onShareClicked()

        assertEquals(null, viewModel.shareEvent.value)
    }

    @Test
    fun pictureOfADayViewModel_on_share_finished() = scope.runTest  {
        `when`(mockSpaceRepository.getPictureOfADay(any())).thenReturn(ResultWrapper.Success(
            successPicture
        ))

        viewModel = PictureOfADayViewModel(mockSpaceRepository)

        viewModel.onShareClicked()
        viewModel.onShareFinished()

        assertEquals(null, viewModel.shareEvent.value)
    }
}