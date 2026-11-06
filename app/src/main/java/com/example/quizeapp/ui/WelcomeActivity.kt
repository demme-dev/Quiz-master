package com.example.quizeapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.quizeapp.R
import com.example.quizeapp.databinding.ActivityWelcomeBinding

/**
 * WelcomeActivity serves as the modern launcher screen of Quiz Master.
 * Features gradient background, pulsing trophy, settings gear icon, and smooth animations.
 */
class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private var pulseAnimator: AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackNavigation()
        setupInsets()
        setupAnimations()
        setupListeners()
    }

    private fun setupBackNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        })
    }

    /**
     * Handles status bar and navigation bar system insets so UI elements stay visible on edge-to-edge screens.
     */
    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutWelcomeRoot) { _, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            binding.btnSettingsGear.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarInset
            }
            binding.btnExit.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBarInset
            }
            insets
        }
    }

    /**
     * Applies entrance animations and continuous slow pulse on the trophy icon card.
     */
    private fun setupAnimations() {
        val slideUpAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        binding.tvAppTitle.startAnimation(slideUpAnim)
        binding.tvAppSubtitle.startAnimation(slideUpAnim)
        binding.btnStartQuiz.startAnimation(slideUpAnim)

        val scaleX = ObjectAnimator.ofFloat(binding.cardIcon, View.SCALE_X, 1.0f, 1.08f, 1.0f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }
        val scaleY = ObjectAnimator.ofFloat(binding.cardIcon, View.SCALE_Y, 1.0f, 1.08f, 1.0f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
        }
        pulseAnimator = AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    /**
     * Configures click events for Start, Settings Gear, and Exit buttons.
     */
    private fun setupListeners() {
        // Start Quiz -> Navigate to Category selection
        binding.btnStartQuiz.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Settings Gear Icon -> Navigate to Settings
        binding.btnSettingsGear.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        // Exit -> Finish activity and close app
        binding.btnExit.setOnClickListener {
            finishAffinity()
        }
    }

    override fun onDestroy() {
        pulseAnimator?.cancel()
        pulseAnimator = null
        super.onDestroy()
    }
}
