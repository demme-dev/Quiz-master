package com.example.quizeapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizeapp.ui.WelcomeActivity

/**
 * Main Activity legacy entry point that redirects directly to WelcomeActivity.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}
