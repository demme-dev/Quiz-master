package com.example.quizeapp.model

import java.io.Serializable

/**
 * Data record storing user's answer details for review in Stage 4.
 *
 * @property questionText Prompt text of the question.
 * @property options List of options displayed to the user.
 * @property userSelectedIndex Option index selected by user, or null if timed out.
 * @property correctIndex Index of the correct option.
 * @property isCorrect True if user answered correctly.
 */
data class UserAnswerRecord(
    val questionText: String,
    val options: List<String>,
    val userSelectedIndex: Int?,
    val correctIndex: Int,
    val isCorrect: Boolean
) : Serializable
