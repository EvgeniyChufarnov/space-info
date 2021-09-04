package com.example.spaceinfo.ui.picturesFromMars

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceinfo.LOADING_BAR_DELAY
import com.example.spaceinfo.domain.data.ResultWrapper
import com.example.spaceinfo.domain.repository.MarsPicturesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class PicturesFromMarsScreenState {
    SHOW_PICTURES, LOADING, ERROR, IDLE
}

@HiltViewModel
class PicturesFromMarsViewModel @Inject constructor(
    private val marsPicturesRepository: MarsPicturesRepository
) : ViewModel() {
    var roverName: String? = null
        set(value) {
            field = value
            _state.value = PicturesFromMarsScreenState.IDLE
            delayToShowProgressBar()
            loadPictures()
        }

    private val _pictures = MutableLiveData<List<String>>()
    val pictures: LiveData<List<String>> = _pictures

    private val _state = MutableLiveData<PicturesFromMarsScreenState>()
    val state: LiveData<PicturesFromMarsScreenState> = _state

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private fun loadPictures() {
        viewModelScope.launch {
            when (val result = roverName?.let { marsPicturesRepository.getPictures(it) }) {
                is ResultWrapper.Success -> {
                    _pictures.value = result.value.photos.map { it.imagePath }
                    _state.value = PicturesFromMarsScreenState.SHOW_PICTURES
                }
                is ResultWrapper.GenericError -> {
                    _errorMessage.value = result.error?.message
                    _state.value = PicturesFromMarsScreenState.ERROR
                }
                is ResultWrapper.NetworkError -> {
                    _errorMessage.value = null
                    _state.value = PicturesFromMarsScreenState.ERROR
                }
            }
        }
    }

    private fun delayToShowProgressBar() {
        viewModelScope.launch {
            delay(LOADING_BAR_DELAY)

            if (state.value == PicturesFromMarsScreenState.IDLE) {
                _state.value = PicturesFromMarsScreenState.LOADING
            }
        }
    }
}