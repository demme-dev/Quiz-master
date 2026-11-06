package com.example.quizeapp.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.quizeapp.databinding.ActivityReviewBinding
import com.example.quizeapp.model.UserAnswerRecord

/**
 * ReviewActivity displays the list of questions, user choices, and correct answers in a RecyclerView.
 */
class ReviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewBinding

    companion object {
        const val EXTRA_RECORDS = "extra_records"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackNavigation()
        setupInsets()

        @Suppress("UNCHECKED_CAST")
        val records = intent.getSerializableExtra(EXTRA_RECORDS) as? List<UserAnswerRecord> ?: emptyList()

        binding.rvReviewQuestions.adapter = ReviewAdapter(records)

        binding.btnBackToResults.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupBackNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Handles status bar and navigation bar system insets so UI elements stay visible on edge-to-edge screens.
     */
    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutReviewRoot) { _, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            binding.btnBack.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarInset
            }
            binding.btnBackToResults.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBarInset
            }
            insets
        }
    }
}
