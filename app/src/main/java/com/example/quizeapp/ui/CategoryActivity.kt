package com.example.quizeapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.quizeapp.R
import com.example.quizeapp.databinding.ActivityCategoryBinding
import com.example.quizeapp.model.Category
import com.example.quizeapp.model.Difficulty

/**
 * CategoryActivity allows users to select a quiz topic and difficulty level.
 */
class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private var selectedCategory: Category = Category.COMPUTER_SCIENCE

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_DIFFICULTY = "extra_difficulty"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackNavigation()
        setupInsets()
        setupCategorySelection()
        setupListeners()
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutCategoryRoot) { _, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            binding.btnBack.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarInset
            }
            binding.btnContinueToQuiz.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBarInset
            }
            insets
        }
    }

    /**
     * Configures category card click listeners and visual stroke highlights.
     */
    private fun setupCategorySelection() {
        val strokeSelected = ContextCompat.getColor(this, R.color.option_stroke_selected)
        val strokeNormal = ContextCompat.getColor(this, R.color.option_stroke_normal)

        fun updateUI(cat: Category) {
            selectedCategory = cat

            binding.rbCatCS.isChecked = (cat == Category.COMPUTER_SCIENCE)
            binding.rbCatGK.isChecked = (cat == Category.GENERAL_KNOWLEDGE)
            binding.rbCatMath.isChecked = (cat == Category.MATHEMATICS)

            binding.cardCatCS.strokeColor = if (cat == Category.COMPUTER_SCIENCE) strokeSelected else strokeNormal
            binding.cardCatCS.strokeWidth = if (cat == Category.COMPUTER_SCIENCE) 4 else 2

            binding.cardCatGK.strokeColor = if (cat == Category.GENERAL_KNOWLEDGE) strokeSelected else strokeNormal
            binding.cardCatGK.strokeWidth = if (cat == Category.GENERAL_KNOWLEDGE) 4 else 2

            binding.cardCatMath.strokeColor = if (cat == Category.MATHEMATICS) strokeSelected else strokeNormal
            binding.cardCatMath.strokeWidth = if (cat == Category.MATHEMATICS) 4 else 2
        }

        binding.cardCatCS.setOnClickListener { updateUI(Category.COMPUTER_SCIENCE) }
        binding.cardCatGK.setOnClickListener { updateUI(Category.GENERAL_KNOWLEDGE) }
        binding.cardCatMath.setOnClickListener { updateUI(Category.MATHEMATICS) }
    }

    /**
     * Sets up click event for the continue button.
     */
    private fun setupListeners() {
        binding.btnContinueToQuiz.setOnClickListener {
            val difficulty = when (binding.rgDifficulty.checkedRadioButtonId) {
                R.id.rbDiffEasy -> Difficulty.EASY
                R.id.rbDiffHard -> Difficulty.HARD
                else -> Difficulty.MEDIUM
            }

            val intent = Intent(this, QuizActivity::class.java).apply {
                putExtra(EXTRA_CATEGORY, selectedCategory.name)
                putExtra(EXTRA_DIFFICULTY, difficulty.name)
            }
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}
