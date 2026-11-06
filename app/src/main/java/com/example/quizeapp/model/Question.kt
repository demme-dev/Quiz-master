package com.example.quizeapp.model

/**
 * Data class representing a multiple-choice question in the quiz.
 *
 * @property text The question prompt text.
 * @property options List of 4 multiple-choice answer strings.
 * @property correctIndex Zero-based index of the correct answer in [options].
 * @property category The topic category this question belongs to.
 */
data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int,
    val category: Category = Category.COMPUTER_SCIENCE
)
