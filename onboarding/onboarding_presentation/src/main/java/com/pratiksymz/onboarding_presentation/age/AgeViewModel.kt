package com.pratiksymz.onboarding_presentation.age

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratiksymz.core.R
import com.pratiksymz.core.domain.preferences.Preferences
import com.pratiksymz.core.domain.use_case.FilterOutDigits
import com.pratiksymz.core.util.UiEvent
import com.pratiksymz.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgeViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigits: FilterOutDigits
) : ViewModel() {
    var age by mutableStateOf("20")
        private set

    private val _uiEvent = Channel<UiEvent>()
    var uiEvent = _uiEvent.receiveAsFlow()

    fun onAgeEnter(age: String) {
        // Age length <= 3
        // Avoid special characters from the Numbers Keyboard
        // NOT ALLOWED HERE -> Use case ->
        if (age.length <= 3) {
            this.age = filterOutDigits(age)
        }
    }

    fun onNextClick() {
        // Sending event into a Channel is suspending
        viewModelScope.launch {
            // Convert to Int if NOT empty
            // If it is empty -> UiEvent Snackbar
            val ageNum = age.toIntOrNull() ?: run {
                _uiEvent.send(
                    UiEvent.ShowSnackbar(
                        UiText.StringResource(R.string.error_age_cant_be_empty)
                    )
                )
                // Return on launch of sending this event
                return@launch
            }
            // Save the age
            preferences.saveAge(ageNum)
            // Navigate to the next onboarding screen (Height)
            _uiEvent.send(UiEvent.Success)
        }
    }
}