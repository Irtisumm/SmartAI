package com.example.citypulseai

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.example.citypulseai.databinding.ActivityEnterOtpBinding

class OtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEnterOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email") ?: "example@gmail.com"
        binding.subtitleText.text = "We have just sent you 4 digit code via your\nemail $email"

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.continueButton.setOnClickListener {
            val otp = binding.otpInputView.text.toString().trim()

            if (otp.length != 4) {
                Toast.makeText(this, "Please enter a 4-digit OTP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (verifyOtp(otp)) {
                Toast.makeText(this, "OTP verified successfully (mocked)", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid OTP (mocked)", Toast.LENGTH_SHORT).show()
            }
        }

        binding.resendCodeLink.setOnClickListener {
            Toast.makeText(this, "Resending OTP to $email (mocked)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun verifyOtp(otp: String): Boolean {
        return otp == "1234"
    }
}