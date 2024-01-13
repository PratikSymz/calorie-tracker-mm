package com.pratiksymz.core.util

/**
 * Events sent from ViewModels to Composables
 */
sealed class UiEvent {
    object Success : UiEvent()
    object NavigateUp : UiEvent()
    data class ShowSnackbar(val message: UiText) : UiEvent()
}