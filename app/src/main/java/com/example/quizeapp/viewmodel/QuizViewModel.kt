package com.example.quizeapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.quizeapp.model.Category
import com.example.quizeapp.model.Difficulty
import com.example.quizeapp.model.QuizQuestionState
import com.example.quizeapp.model.QuizSettings
import com.example.quizeapp.model.UserAnswerRecord
import com.example.quizeapp.repository.QuestionRepository

/**
 * ViewModel managing quiz state, shuffling, instant feedback, timer, and results.
 */
class QuizViewModel : ViewModel() {

    companion object {
        // High score maintained across sessions in memory
        var sessionBestScore: Int = 0
            private set

        fun updateSessionBestScore(score: Int) {
            if (score > sessionBestScore) {
                sessionBestScore = score
            }
        }
    }

    var category: Category = Category.COMPUTER_SCIENCE
        private set

    var difficulty: Difficulty = Difficulty.MEDIUM
        private set

    // List of questions with shuffled options for current quiz run
    var questionsState: List<QuizQuestionState> = emptyList()
        private set

    var currentIndex: Int = 0
        private set

    var score: Int = 0
        private set

    var selectedOptionIndex: Int? = null
        private set

    var isAnswerSubmitted: Boolean = false
        private set

    var isQuizFinished: Boolean = false
        private set

    // Timer state
    var maxTimerSeconds: Int = 20
        private set

    var remainingTimerSeconds: Int = 20
        private set

    // History of answered questions for review screen
    val userAnswerRecords = mutableListOf<UserAnswerRecord>()

    private var isInitialized: Boolean = false

    /**
     * Initializes the quiz session with selected Category, Difficulty, and Question count.
     * Prevents re-shuffling on configuration changes / rotation.
     */
    fun initQuiz(
        selectedCategory: Category,
        selectedDifficulty: Difficulty,
        requestedCount: Int = QuizSettings.questionCount
    ) {
        if (isInitialized) return

        category = selectedCategory
        difficulty = selectedDifficulty

        // If timer disabled in settings or difficulty is EASY, maxTimerSeconds = 0
        maxTimerSeconds = if (!QuizSettings.isTimerEnabled || selectedDifficulty == Difficulty.EASY) {
            0
        } else {
            selectedDifficulty.timerSeconds
        }
        remainingTimerSeconds = maxTimerSeconds

        // Load questions for category, shuffle order, and limit by requestedCount
        val rawQuestions = QuestionRepository.getQuestionsForCategory(selectedCategory)
            .shuffled()
            .take(requestedCount)

        // Shuffle options for each question
        questionsState = rawQuestions.map { question ->
            val shuffledOptions = question.options.shuffled()
            val correctOptionText = question.options[question.correctIndex]
            val correctShuffledIndex = shuffledOptions.indexOf(correctOptionText)

            QuizQuestionState(
                originalQuestion = question,
                shuffledOptions = shuffledOptions,
                correctShuffledIndex = correctShuffledIndex
            )
        }

        currentIndex = 0
        score = 0
        selectedOptionIndex = null
        isAnswerSubmitted = false
        isQuizFinished = false
        userAnswerRecords.clear()

        isInitialized = true
    }

    val totalQuestions: Int
        get() = questionsState.size

    val currentQuestionState: QuizQuestionState
        get() = questionsState[currentIndex]

    val isLastQuestion: Boolean
        get() = currentIndex == totalQuestions - 1

    val percentageScore: Int
        get() = if (totalQuestions > 0) ((score.toDouble() / totalQuestions) * 100).toInt() else 0

    /**
     * User selects an option index (0..3).
     */
    fun selectOption(index: Int) {
        if (isAnswerSubmitted) return
        selectedOptionIndex = index
    }

    /**
     * Updates remaining countdown seconds during timer tick.
     */
    fun updateRemainingTime(seconds: Int) {
        remainingTimerSeconds = seconds
    }

    /**
     * Submits the selected answer (or times out if null) and shows instant feedback.
     * Award 1 point per correct answer.
     *
     * @return true if an answer was submitted or timed out; false if user tapped submit with no selection.
     */
    fun submitAnswer(isTimeOut: Boolean = false): Boolean {
        if (isAnswerSubmitted) return false
        if (selectedOptionIndex == null && !isTimeOut) return false

        val selectedIndex = selectedOptionIndex
        val correctIndex = currentQuestionState.correctShuffledIndex
        val isCorrect = selectedIndex == correctIndex

        if (isCorrect) {
            score++
        }

        // Record for Stage 4 Review Screen
        userAnswerRecords.add(
            UserAnswerRecord(
                questionText = currentQuestionState.originalQuestion.text,
                options = currentQuestionState.shuffledOptions,
                userSelectedIndex = selectedIndex,
                correctIndex = correctIndex,
                isCorrect = isCorrect
            )
        )

        isAnswerSubmitted = true
        updateSessionBestScore(score)
        return true
    }

    /**
     * Advances to the next question or finishes quiz if on the last question.
     *
     * @return true if moved to next question; false if quiz completed.
     */
    fun nextQuestion(): Boolean {
        if (isLastQuestion) {
            isQuizFinished = true
            updateSessionBestScore(score)
            return false
        } else {
            currentIndex++
            selectedOptionIndex = null
            isAnswerSubmitted = false
            remainingTimerSeconds = maxTimerSeconds
            return true
        }
    }

    /**
     * Resets state for a new quiz session.
     */
    fun restartQuiz() {
        isInitialized = false
        initQuiz(category, difficulty)
    }
}
