package com.example.citypulseai

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.citypulseai.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.inputPassword.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= binding.inputPassword.right - binding.inputPassword.compoundDrawables[2].bounds.width()) {
                    isPasswordVisible = !isPasswordVisible
                    if (isPasswordVisible) {
                        binding.inputPassword.transformationMethod = SingleLineTransformationMethod.getInstance()
                        binding.inputPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye, 0)
                    } else {
                        binding.inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        binding.inputPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_slash, 0)
                    }
                    binding.inputPassword.setSelection(binding.inputPassword.text.length)
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.createAccountButton.setOnClickListener {
            val fullName = binding.inputFullname.text.toString().trim()
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.inputEmail.error = "Invalid email format"
                return@setOnClickListener
            }
            if (password.length < 6) {
                binding.inputPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            Toast.makeText(this, "Account created successfully (mocked)", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.googleSignupButton.setOnClickListener {
            Toast.makeText(this, "Google Sign-Up not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }
}