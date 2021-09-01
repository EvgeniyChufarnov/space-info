package com.example.spaceinfo.ui.pictureOfADay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceinfo.domain.data.ResultWrapper
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity
import com.example.spaceinfo.domain.repository.SpaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val DATE_FORMAT_PATTEN = "yyyy-MM-dd"
private const val LOADING_BAR_DELAY = 300L

enum class PictureOfADayScreenState {
    SHOW_PICTURE, SHOW_VIDEO, LOADING, ERROR, IDLE
}

enum class PictureDateChoice(val days: Int) {
    TODAY(0), YESTERDAY(-1), TWO_DAYS_AGO(-2)
}

@HiltViewModel
class PictureOfADayViewModel @Inject constructor(
    private val spaceRepository: SpaceRepository
) : ViewModel() {
    private val dateFormat: DateFormat = SimpleDateFormat(DATE_FORMAT_PATTEN, Locale.getDefault())
    private var currentDate = PictureDateChoice.TODAY

    private val _picture = MutableLiveData<PictureOfADayEntity>()
    val picture: LiveData<PictureOfADayEntity> = _picture

    private val _state = MutableLiveData<PictureOfADayScreenState>()
    val state: LiveData<PictureOfADayScreenState> = _state

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _shareEvent = MutableLiveData<PictureOfADayEntity?>()
    val shareEvent: LiveData<PictureOfADayEntity?> = _shareEvent

    init {
        _state.value = PictureOfADayScreenState.IDLE
        loadPictureOfADay(dateFormat.format(Date()))
        delayToShowProgressBar()
    }

    fun onAnotherDatePictureClicked(choice: PictureDateChoice) {
        if (choice == currentDate) return

        _state.value = PictureOfADayScreenState.IDLE

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, choice.days)
        loadPictureOfADay(dateFormat.format(calendar.time))
        currentDate = choice

        delayToShowProgressBar()
    }

    private fun loadPictureOfADay(date: String) {
        viewModelScope.launch {
            when (val result = spaceRepository.getPictureOfADay(date)) {
                is ResultWrapper.Success -> {
                    _picture.value = result.value!!

                    if (result.value.isVideo()) {
                        _state.value = PictureOfADayScreenState.SHOW_VIDEO
                    }
                }
                is ResultWrapper.GenericError -> {
                    _errorMessage.value = result.error?.message
                    _state.value = PictureOfADayScreenState.ERROR
                }
                is ResultWrapper.NetworkError -> {
                    _errorMessage.value = null
                    _state.value = PictureOfADayScreenState.ERROR
                }
            }
        }
    }

    fun onPictureReady() {
        _state.value = PictureOfADayScreenState.SHOW_PICTURE
    }

    fun onShareClicked() {
        if (state.value != PictureOfADayScreenState.ERROR && state.value != PictureOfADayScreenState.LOADING) {
            _shareEvent.value = picture.value
        } else {
            _errorMessage.value = null
        }
    }

    fun onShareFinished() {
        _shareEvent.value = null
    }

    private fun delayToShowProgressBar() {
        viewModelScope.launch {
            delay(LOADING_BAR_DELAY)

            if (state.value == PictureOfADayScreenState.IDLE) {
                _state.value = PictureOfADayScreenState.LOADING
            }
        }
    }
}