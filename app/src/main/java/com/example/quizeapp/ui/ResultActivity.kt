package com.example.quizeapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.quizeapp.R
import com.example.quizeapp.databinding.ActivityResultBinding
import com.example.quizeapp.model.UserAnswerRecord

/**
 * ResultActivity displays quiz score summary, session high score, review trigger, and social sharing.
 */
class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    companion object {
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_TOTAL = "extra_total"
        const val EXTRA_PERCENTAGE = "extra_percentage"
        const val EXTRA_BEST_SCORE = "extra_best_score"
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
        const val EXTRA_RECORDS = "extra_records"
    }

    private var score: Int = 0
    private var totalQuestions: Int = 10
    private var percentage: Int = 0
    private var bestScore: Int = 0
    private var categoryName: String = "Computer Science"
    private var records: ArrayList<UserAnswerRecord> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackNavigation()
        setupInsets()
        extractIntentExtras()
        displayResults()
        setupListeners()
    }

    private fun setupBackNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToWelcome()
            }
        })
    }

    /**
     * Handles status bar and navigation bar system insets so UI elements stay visible on edge-to-edge screens.
     */
    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutResultRoot) { _, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            binding.tvTrophyEmoji.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarInset
            }
            binding.layoutBottomRow.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBarInset
            }
            insets
        }
    }

    private fun navigateToWelcome() {
        val intent = Intent(this, WelcomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(intent)
        finish()
    }

    private fun extractIntentExtras() {
        score = intent.getIntExtra(EXTRA_SCORE, 0)
        totalQuestions = intent.getIntExtra(EXTRA_TOTAL, 10)
        percentage = intent.getIntExtra(EXTRA_PERCENTAGE, 0)
        bestScore = intent.getIntExtra(EXTRA_BEST_SCORE, score)
        categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: "Quiz"

        @Suppress("UNCHECKED_CAST")
        val recs = intent.getSerializableExtra(EXTRA_RECORDS) as? ArrayList<UserAnswerRecord>
        if (recs != null) {
            records = recs
        }
    }

    private fun displayResults() {
        binding.tvPercentage.text = getString(R.string.percentage_format, percentage)
        binding.tvFinalScore.text = getString(R.string.final_score_format, score, totalQuestions)
        binding.tvBestScore.text = getString(R.string.best_score_format, bestScore, totalQuestions)

        val messageRes = when {
            percentage >= 80 -> R.string.msg_excellent
            percentage >= 50 -> R.string.msg_good
            else -> R.string.msg_keep_practicing
        }
        binding.tvResultMessage.text = getString(messageRes)
    }

    private fun setupListeners() {
        // Review Answers
        binding.btnReviewAnswers.setOnClickListener {
            val intent = Intent(this, ReviewActivity::class.java).apply {
                putExtra(ReviewActivity.EXTRA_RECORDS, records)
            }
            startActivity(intent)
        }

        // Play Again -> Return to Category Selection
        binding.btnPlayAgain.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }

        // Home -> Return to Welcome Screen
        binding.btnHome.setOnClickListener {
            navigateToWelcome()
        }

        // Share Result -> ACTION_SEND Intent
        binding.btnShare.setOnClickListener {
            val shareText = getString(
                R.string.share_text_format,
                score,
                totalQuestions,
                percentage,
                categoryName
            )
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareText)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_result))
            startActivity(shareIntent)
        }
    }
}
