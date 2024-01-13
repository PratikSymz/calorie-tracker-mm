package com.pratiksymz.onboarding_presentation.gender

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratiksymz.core.domain.models.Gender
import com.pratiksymz.core.domain.preferences.Preferences
import com.pratiksymz.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor(
    private val preferences: Preferences
) : ViewModel() {
    var selectedGender by mutableStateOf<Gender>(Gender.Male)   // Type of Gender NOT Gender.MALE
        private set     // Set only within VM, but observable from outside

    /**
     * Channel: Send one-time events to the UI
     * Using UiEvent to send events from VM to UI, such as show Snackbar or Validation was successful
     */
    // _ -> ViewModel version to set events, but UI should receive a channel or a flow to only observe the changes and
    // not send something back
    private val _uiEvent = Channel<UiEvent>()
    // This flow can be observed in the UI and triggered at most once for every single event
    // Not considered as a state since these events won't trigger again on screen rotation
    // Example, show snackbar -> Only once
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onGenderSelect(gender: Gender) {
        selectedGender = gender
    }

    fun onNextClick() {
        // Sending event into a Channel is suspending
        viewModelScope.launch {
            // Save the gender
            preferences.saveGender(selectedGender)
            // Navigate to the next onboarding screen (Age)
            _uiEvent.send(UiEvent.Success)
        }
    }
}