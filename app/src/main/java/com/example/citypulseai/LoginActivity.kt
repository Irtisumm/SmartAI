package com.example.citypulseai

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.citypulseai.databinding.ActivityLoginPageBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
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

        binding.signinButton.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.inputEmail.error = "Invalid email format"
                return@setOnClickListener
            }

            if (email == "test@example.com" && password == "password") {
                Toast.makeText(this, "Login successful (mocked)", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Login failed (mocked): Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.forgotPasswordLink.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.signupLink.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.googleSigninButton.setOnClickListener {
            Toast.makeText(this, "Google Sign-In not implemented yet", Toast.LENGTH_SHORT).show()
        }

        binding.rememberMeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Remember Me selected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}