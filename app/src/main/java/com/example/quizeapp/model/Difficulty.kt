package com.example.quizeapp.model

/**
 * Enum representing difficulty levels and associated per-question timer limits.
 *
 * @property displayName Human-readable label.
 * @property timerSeconds Per-question time limit in seconds (0 means no timer).
 */
enum class Difficulty(val displayName: String, val timerSeconds: Int) {
    EASY("Easy", 0),
    MEDIUM("Medium", 20),
    HARD("Hard", 10)
}
