package com.example.spaceinfo.ui.pictureOfADay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceinfo.domain.repository.SpaceRepository
import com.example.spaceinfo.domain.data.ResultWrapper
import com.example.spaceinfo.domain.data.entities.PictureOfADayEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val DATE_FORMAT_PATTEN = "yyyy-MM-dd"

enum class PictureDateState {
    SHOW_PICTURE, SHOW_VIDEO, LOADING, ERROR
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

    private val _state = MutableLiveData<PictureDateState>()
    val state: LiveData<PictureDateState> = _state

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _shareEvent = MutableLiveData<PictureOfADayEntity?>()
    val shareEvent: LiveData<PictureOfADayEntity?> = _shareEvent

    init {
        _state.value = PictureDateState.LOADING
        getPictureOfADay(dateFormat.format(Date()))
    }

    fun onAnotherDatePictureClicked(choice: PictureDateChoice) {
        if (choice == currentDate) return

        _state.value = PictureDateState.LOADING

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, choice.days)
        getPictureOfADay(dateFormat.format(calendar.time))
        currentDate = choice
    }

    private fun getPictureOfADay(date: String) {
        viewModelScope.launch {
            when (val result = spaceRepository.getPictureOfADay(date)) {
                is ResultWrapper.Success -> {
                    _picture.value = result.value!!

                    if (result.value.isVideo()) {
                        _state.value = PictureDateState.SHOW_VIDEO
                    }
                }
                is ResultWrapper.GenericError -> {
                    _errorMessage.value = result.error?.message
                    _state.value = PictureDateState.ERROR
                }
                is ResultWrapper.NetworkError -> {
                    _errorMessage.value = null
                    _state.value = PictureDateState.ERROR
                }
            }
        }
    }

    fun onPictureReady() {
        _state.value = PictureDateState.SHOW_PICTURE
    }

    fun onShareClicked() {
        if (state.value != PictureDateState.ERROR && state.value != PictureDateState.LOADING) {
            _shareEvent.value = picture.value
        } else {
            _errorMessage.value = null
        }
    }

    fun onShareFinished() {
        _shareEvent.value = null
    }
}