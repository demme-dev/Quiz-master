package com.example.quizeapp.model

/**
 * Encapsulates a question with its shuffled options order for a quiz run.
 *
 * @property originalQuestion The source [Question].
 * @property shuffledOptions List of 4 option strings in shuffled order.
 * @property correctShuffledIndex Index of the correct answer in [shuffledOptions].
 */
data class QuizQuestionState(
    val originalQuestion: Question,
    val shuffledOptions: List<String>,
    val correctShuffledIndex: Int
)
