package com.example.citypulseai

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.citypulseai.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.nextButton.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()

            if (email.isEmpty()) {
                binding.inputEmail.error = "Please enter your email"
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.inputEmail.error = "Invalid email format"
                return@setOnClickListener
            }

            Toast.makeText(this, "Reset link sent to $email (mocked)", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra("email", email)
            startActivity(intent)
        }
    }
}