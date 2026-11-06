package com.example.quizeapp.model

/**
 * In-memory settings object managing global app preferences.
 *
 * @property isSoundEnabled Toggle for sound effects (default: true).
 * @property isTimerEnabled Toggle for per-question countdown timer (default: true).
 * @property questionCount Number of questions per quiz (options: 5, 10, 15 - default: 10).
 */
object QuizSettings {
    var isSoundEnabled: Boolean = true
    var isTimerEnabled: Boolean = true
    var questionCount: Int = 10
}
