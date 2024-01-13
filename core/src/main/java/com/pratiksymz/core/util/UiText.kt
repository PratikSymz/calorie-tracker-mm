package com.pratiksymz.core.util

import android.content.Context

/**
 * This class provides the options to either return a string as a "dynamic" string from an API or hardcoded
 * Or, you return the string from a resource Id using the application context.
 */
sealed class UiText {
    data class DynamicString(val text: String) : UiText()
    data class StringResource(val resId: Int) : UiText()

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> text
            is StringResource -> context.getString(resId)
        }
    }
}