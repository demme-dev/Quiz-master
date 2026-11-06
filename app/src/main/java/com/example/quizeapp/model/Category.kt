package com.example.quizeapp.model

/**
 * Enum representing quiz topics available in Quiz Master.
 *
 * @property displayName Human-readable name of the category.
 */
enum class Category(val displayName: String) {
    COMPUTER_SCIENCE("Computer Science"),
    GENERAL_KNOWLEDGE("General Knowledge"),
    MATHEMATICS("Mathematics")
}
