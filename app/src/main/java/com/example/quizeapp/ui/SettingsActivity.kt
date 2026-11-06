package com.example.quizeapp.ui

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import com.example.quizeapp.R
import com.example.quizeapp.databinding.ActivitySettingsBinding
import com.example.quizeapp.model.QuizSettings
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * SettingsActivity provides configuration options for sound, timer, question count, help, and app info.
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackNavigation()
        setupInsets()
        loadCurrentSettings()
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
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutSettingsRoot) { _, insets ->
            val statusBarInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            binding.btnBack.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarInset
            }
            insets
        }
    }

    /**
     * Loads saved settings from QuizSettings into the UI controls.
     */
    private fun loadCurrentSettings() {
        binding.switchSound.isChecked = QuizSettings.isSoundEnabled
        binding.switchTimer.isChecked = QuizSettings.isTimerEnabled

        when (QuizSettings.questionCount) {
            5 -> binding.toggleQuestionCount.check(R.id.btnQc5)
            15 -> binding.toggleQuestionCount.check(R.id.btnQc15)
            else -> binding.toggleQuestionCount.check(R.id.btnQc10)
        }
    }

    /**
     * Sets up click listeners for back navigation, switch toggles, question count selection, and help dialog.
     */
    private fun setupListeners() {
        binding.switchSound.setOnCheckedChangeListener { _, isChecked ->
            QuizSettings.isSoundEnabled = isChecked
        }

        binding.switchTimer.setOnCheckedChangeListener { _, isChecked ->
            QuizSettings.isTimerEnabled = isChecked
        }

        binding.toggleQuestionCount.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                QuizSettings.questionCount = when (checkedId) {
                    R.id.btnQc5 -> 5
                    R.id.btnQc15 -> 15
                    else -> 10
                }
            }
        }

        binding.rowHowToPlay.setOnClickListener {
            showHowToPlayDialog()
        }
    }

    /**
     * Displays formatted rules dialog with numbered items and a "Got it" button.
     */
    private fun showHowToPlayDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.how_to_play_title))
            .setMessage(getString(R.string.how_to_play_rules))
            .setPositiveButton(getString(R.string.btn_got_it)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
