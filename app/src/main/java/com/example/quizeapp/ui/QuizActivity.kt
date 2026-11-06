package com.example.quizeapp.ui

import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.quizeapp.R
import com.example.quizeapp.databinding.ActivityQuizBinding
import com.example.quizeapp.model.Category
import com.example.quizeapp.model.Difficulty
import com.example.quizeapp.model.QuizSettings
import com.example.quizeapp.viewmodel.QuizViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * QuizActivity handles the quiz screen, sound effects, timer, instant feedback, and navigation.
 */
class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()

    private var countDownTimer: CountDownTimer? = null
    private lateinit var optionCards: List<MaterialCardView>

    // SoundPool audio
    private var soundPool: SoundPool? = null
    private var soundCorrectId: Int = 0
    private var soundWrongId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryName = intent.getStringExtra(CategoryActivity.EXTRA_CATEGORY) ?: Category.COMPUTER_SCIENCE.name
        val difficultyName = intent.getStringExtra(CategoryActivity.EXTRA_DIFFICULTY) ?: Difficulty.MEDIUM.name

        val category = try { Category.valueOf(categoryName) } catch (e: Exception) { Category.COMPUTER_SCIENCE }
        val difficulty = try { Difficulty.valueOf(difficultyName) } catch (e: Exception) { Difficulty.MEDIUM }

        viewModel.initQuiz(category, difficulty)

        optionCards = listOf(
            binding.cardOption1,
            binding.cardOption2,
            binding.cardOption3,
            binding.cardOption4
        )

        setupBackNavigation()
        setupInsets()
        setupSoundPool()
        setupListeners()
        updateUI()
    }

    private fun setupBackNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showQuitConfirmationDialog()
            }
        })
    }

    /**
     * Handles status bar and navigation bar system insets so UI elements stay visible on edge-to-edge screens.
     */
    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutQuizRoot) { _, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            binding.tvQuestionProgress.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarInset
            }
            binding.tvCurrentScore.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarInset
            }
            binding.btnMuteToggle.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarInset
            }
            binding.btnSubmitNext.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBarInset
            }
            insets
        }
    }

    private fun showQuitConfirmationDialog() {
        stopTimer()
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.quit_quiz_title))
            .setMessage(getString(R.string.quit_quiz_message))
            .setPositiveButton(getString(R.string.btn_quit)) { dialog, _ ->
                dialog.dismiss()
                stopTimer()
                finish()
            }
            .setNegativeButton(getString(R.string.btn_continue_quiz)) { dialog, _ ->
                dialog.dismiss()
                if (!isFinishing && !isDestroyed && !viewModel.isAnswerSubmitted) {
                    startTimerIfNeeded()
                }
            }
            .setOnCancelListener {
                if (!isFinishing && !isDestroyed && !viewModel.isAnswerSubmitted) {
                    startTimerIfNeeded()
                }
            }
            .show()
    }

    private fun setupSoundPool() {
        soundPool = SoundPool.Builder()
            .setMaxStreams(2)
            .build()

        soundCorrectId = soundPool?.load(this, R.raw.sound_correct, 1) ?: 0
        soundWrongId = soundPool?.load(this, R.raw.sound_wrong, 1) ?: 0
    }

    private fun playSound(isCorrect: Boolean) {
        if (!QuizSettings.isSoundEnabled || soundPool == null) return
        val soundId = if (isCorrect) soundCorrectId else soundWrongId
        if (soundId != 0) {
            soundPool?.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f)
        }
    }

    /**
     * Sets up click listeners for option cards, submit/next button, and sound toggle.
     */
    private fun setupListeners() {
        optionCards.forEachIndexed { index, card ->
            card.setOnClickListener {
                if (!viewModel.isAnswerSubmitted) {
                    viewModel.selectOption(index)
                    renderOptionSelections()
                }
            }
        }

        binding.btnSubmitNext.setOnClickListener {
            if (!viewModel.isAnswerSubmitted) {
                val success = viewModel.submitAnswer()
                if (!success) {
                    Toast.makeText(this, getString(R.string.please_select_answer), Toast.LENGTH_SHORT).show()
                } else {
                    stopTimer()
                    val isCorrect = (viewModel.selectedOptionIndex == viewModel.currentQuestionState.correctShuffledIndex)
                    playSound(isCorrect)
                    updateUI()
                }
            } else {
                val hasMore = viewModel.nextQuestion()
                if (hasMore) {
                    updateUI()
                } else {
                    stopTimer()
                    navigateToResultScreen()
                }
            }
        }

        binding.btnMuteToggle.setOnClickListener {
            QuizSettings.isSoundEnabled = !QuizSettings.isSoundEnabled
            updateMuteButtonIcon()
        }
    }

    private fun updateMuteButtonIcon() {
        binding.btnMuteToggle.text = if (QuizSettings.isSoundEnabled) "🔊" else "🔇"
    }

    /**
     * Updates the UI elements based on current ViewModel state.
     */
    private fun updateUI() {
        val state = viewModel.currentQuestionState

        // Progress text and score ("Score: 7 / 10")
        binding.tvQuestionProgress.text = getString(
            R.string.question_progress_format,
            viewModel.currentIndex + 1,
            viewModel.totalQuestions
        )
        binding.tvCurrentScore.text = getString(
            R.string.current_score_format,
            viewModel.score,
            viewModel.totalQuestions
        )

        updateMuteButtonIcon()

        // Progress bar
        binding.progressBarQuiz.max = viewModel.totalQuestions
        binding.progressBarQuiz.progress = viewModel.currentIndex + 1

        // Question text
        binding.tvQuestionText.text = state.originalQuestion.text

        // Set option texts
        binding.tvOption1.text = state.shuffledOptions[0]
        binding.tvOption2.text = state.shuffledOptions[1]
        binding.tvOption3.text = state.shuffledOptions[2]
        binding.tvOption4.text = state.shuffledOptions[3]

        if (viewModel.isAnswerSubmitted) {
            renderInstantFeedback()
            if (viewModel.isLastQuestion) {
                binding.btnSubmitNext.text = getString(R.string.finish_button)
            } else {
                binding.btnSubmitNext.text = getString(R.string.next_button)
            }
        } else {
            renderOptionSelections()
            binding.btnSubmitNext.text = getString(R.string.submit_button)
            startTimerIfNeeded()
        }
    }

    /**
     * Renders option card selection highlight and ensures cards are enabled for new questions.
     */
    private fun renderOptionSelections() {
        val strokeSelected = ContextCompat.getColor(this, R.color.option_stroke_selected)
        val strokeNormal = ContextCompat.getColor(this, R.color.option_stroke_normal)
        val bgNormal = ContextCompat.getColor(this, R.color.option_bg_normal)

        optionCards.forEachIndexed { index, card ->
            card.visibility = View.VISIBLE
            card.isEnabled = !viewModel.isAnswerSubmitted
            card.isClickable = !viewModel.isAnswerSubmitted

            val isSelected = (index == viewModel.selectedOptionIndex)
            card.setCardBackgroundColor(bgNormal)
            card.strokeColor = if (isSelected) strokeSelected else strokeNormal
            card.strokeWidth = if (isSelected) 4 else 2
        }
    }

    /**
     * Renders green/red instant feedback when an answer is submitted.
     */
    private fun renderInstantFeedback() {
        val correctIndex = viewModel.currentQuestionState.correctShuffledIndex
        val userIndex = viewModel.selectedOptionIndex

        val greenBg = ContextCompat.getColor(this, R.color.correct_green_bg)
        val greenStroke = ContextCompat.getColor(this, R.color.correct_green)
        val redBg = ContextCompat.getColor(this, R.color.wrong_red_bg)
        val redStroke = ContextCompat.getColor(this, R.color.wrong_red)
        val normalBg = ContextCompat.getColor(this, R.color.option_bg_normal)
        val normalStroke = ContextCompat.getColor(this, R.color.option_stroke_normal)

        optionCards.forEachIndexed { index, card ->
            card.isClickable = false
            card.isEnabled = false
            when (index) {
                correctIndex -> {
                    card.setCardBackgroundColor(greenBg)
                    card.strokeColor = greenStroke
                    card.strokeWidth = 4
                }
                userIndex -> {
                    card.setCardBackgroundColor(redBg)
                    card.strokeColor = redStroke
                    card.strokeWidth = 4
                }
                else -> {
                    card.setCardBackgroundColor(normalBg)
                    card.strokeColor = normalStroke
                    card.strokeWidth = 1
                }
            }
        }
    }

    /**
     * Starts countdown timer if difficulty specifies a timer limit and timer setting is enabled.
     */
    private fun startTimerIfNeeded() {
        stopTimer()
        val maxSeconds = viewModel.maxTimerSeconds

        if (maxSeconds <= 0) {
            binding.layoutTimer.visibility = View.GONE
            return
        }

        binding.layoutTimer.visibility = View.VISIBLE
        binding.progressBarTimer.max = maxSeconds

        val remainingMillis = viewModel.remainingTimerSeconds * 1000L

        countDownTimer = object : CountDownTimer(remainingMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                viewModel.updateRemainingTime(secondsLeft)
                binding.progressBarTimer.progress = secondsLeft
                binding.tvTimerText.text = getString(R.string.timer_format, secondsLeft)
            }

            override fun onFinish() {
                viewModel.updateRemainingTime(0)
                binding.progressBarTimer.progress = 0
                binding.tvTimerText.text = getString(R.string.time_up)
                viewModel.submitAnswer(isTimeOut = true)
                playSound(isCorrect = false)
                updateUI()
            }
        }.start()
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    private fun navigateToResultScreen() {
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra(ResultActivity.EXTRA_SCORE, viewModel.score)
            putExtra(ResultActivity.EXTRA_TOTAL, viewModel.totalQuestions)
            putExtra(ResultActivity.EXTRA_PERCENTAGE, viewModel.percentageScore)
            putExtra(ResultActivity.EXTRA_BEST_SCORE, QuizViewModel.sessionBestScore)
            putExtra(ResultActivity.EXTRA_CATEGORY_NAME, viewModel.category.displayName)
            putExtra(ResultActivity.EXTRA_RECORDS, ArrayList(viewModel.userAnswerRecords))
        }
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    override fun onDestroy() {
        stopTimer()
        soundPool?.release()
        soundPool = null
        super.onDestroy()
    }
}
