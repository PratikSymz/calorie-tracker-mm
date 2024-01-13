package com.example.core.domain.use_case

/**
 * This class has only one functionality (use-case) to filter out digits from a string
 */
class FilterOutDigits {
    operator fun invoke(text: String): String {
        return text.filter { it.isDigit() }
    }
}