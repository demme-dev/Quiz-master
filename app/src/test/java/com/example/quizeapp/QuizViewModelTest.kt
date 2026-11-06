package com.example.quizeapp

import com.example.quizeapp.model.Category
import com.example.quizeapp.model.Difficulty
import com.example.quizeapp.viewmodel.QuizViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuizViewModelTest {

    private lateinit var viewModel: QuizViewModel

    @Before
    fun setUp() {
        viewModel = QuizViewModel()
        viewModel.initQuiz(Category.COMPUTER_SCIENCE, Difficulty.MEDIUM, 10)
    }

    @Test
    fun initQuiz_loadsCategoryQuestionsAndInitialState() {
        assertEquals(Category.COMPUTER_SCIENCE, viewModel.category)
        assertEquals(Difficulty.MEDIUM, viewModel.difficulty)
        assertEquals(10, viewModel.totalQuestions)
        assertEquals(0, viewModel.currentIndex)
        assertEquals(0, viewModel.score)
        assertNull(viewModel.selectedOptionIndex)
        assertFalse(viewModel.isAnswerSubmitted)
        assertFalse(viewModel.isQuizFinished)
    }

    @Test
    fun selectOption_updatesSelectionWhenNotSubmitted() {
        viewModel.selectOption(2)
        assertEquals(2, viewModel.selectedOptionIndex)
    }

    @Test
    fun submitAnswer_correctSelection_incrementsScoreByOne() {
        val correctIndex = viewModel.currentQuestionState.correctShuffledIndex
        viewModel.selectOption(correctIndex)

        val success = viewModel.submitAnswer()
        assertTrue(success)
        assertTrue(viewModel.isAnswerSubmitted)
        assertEquals(1, viewModel.score)
        assertEquals(1, viewModel.userAnswerRecords.size)
        assertTrue(viewModel.userAnswerRecords.first().isCorrect)
    }

    @Test
    fun submitAnswer_incorrectSelection_doesNotIncrementScore() {
        val correctIndex = viewModel.currentQuestionState.correctShuffledIndex
        val wrongIndex = (correctIndex + 1) % 4
        viewModel.selectOption(wrongIndex)

        val success = viewModel.submitAnswer()
        assertTrue(success)
        assertTrue(viewModel.isAnswerSubmitted)
        assertEquals(0, viewModel.score)
        assertEquals(1, viewModel.userAnswerRecords.size)
        assertFalse(viewModel.userAnswerRecords.first().isCorrect)
    }

    @Test
    fun submitAnswer_timeOut_marksUnansweredAndIncorrect() {
        val success = viewModel.submitAnswer(isTimeOut = true)
        assertTrue(success)
        assertTrue(viewModel.isAnswerSubmitted)
        assertEquals(0, viewModel.score)
        assertNull(viewModel.userAnswerRecords.last().userSelectedIndex)
        assertFalse(viewModel.userAnswerRecords.last().isCorrect)
    }

    @Test
    fun completeQuiz_calculatesSimpleScoreAndPercentage() {
        repeat(viewModel.totalQuestions) {
            val correctIndex = viewModel.currentQuestionState.correctShuffledIndex
            viewModel.selectOption(correctIndex)
            viewModel.submitAnswer()
            viewModel.nextQuestion()
        }

        assertTrue(viewModel.isQuizFinished)
        assertEquals(10, viewModel.score)
        assertEquals(100, viewModel.percentageScore)
        assertEquals(10, QuizViewModel.sessionBestScore)
    }

    @Test
    fun nextQuestion_resetsAnswerSubmittedStateAndClearsSelections() {
        val correctIndex = viewModel.currentQuestionState.correctShuffledIndex
        viewModel.selectOption(correctIndex)
        viewModel.submitAnswer()

        assertTrue(viewModel.isAnswerSubmitted)

        val hasNext = viewModel.nextQuestion()
        assertTrue(hasNext)
        assertEquals(1, viewModel.currentIndex)
        assertFalse(viewModel.isAnswerSubmitted)
        assertNull(viewModel.selectedOptionIndex)
        assertEquals(viewModel.maxTimerSeconds, viewModel.remainingTimerSeconds)
    }

    @Test
    fun restartQuiz_resetsState() {
        viewModel.selectOption(viewModel.currentQuestionState.correctShuffledIndex)
        viewModel.submitAnswer()
        viewModel.nextQuestion()

        viewModel.restartQuiz()

        assertEquals(0, viewModel.currentIndex)
        assertEquals(0, viewModel.score)
        assertNull(viewModel.selectedOptionIndex)
        assertFalse(viewModel.isAnswerSubmitted)
        assertFalse(viewModel.isQuizFinished)
    }
}
