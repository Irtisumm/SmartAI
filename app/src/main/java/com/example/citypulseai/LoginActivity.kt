package com.example.citypulseai

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.citypulseai.databinding.ActivityLoginPageBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private var isPasswordVisible = false
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        // Google Sign-In configuration
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

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
                Log.w(TAG, "Empty email or password")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        Log.i(TAG, "Login successful for $email")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        val errorMsg = task.exception?.message ?: "Unknown error"
                        Toast.makeText(this, "Login failed: $errorMsg", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Login failed: $errorMsg", task.exception)
                    }
                }
        }

        binding.forgotPasswordLink.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.signupLink.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // Google Sign-In button click
        binding.googleSigninButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        binding.rememberMeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Remember Me selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account?.idToken == null) {
                    Toast.makeText(this, "Google sign-in failed: No ID token", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Google sign-in failed: No ID token")
                    return
                }
                firebaseAuthWithGoogle(account.idToken)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Google sign-in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        if (idToken.isNullOrEmpty()) {
            Toast.makeText(this, "Google sign-in failed: Empty ID token", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Google sign-in failed: Empty ID token")
            return
        }
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Google sign-in successful!", Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "Google sign-in successful")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    val errorMsg = task.exception?.message ?: "Unknown error"
                    Toast.makeText(this, "Google sign-in failed: $errorMsg", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Google sign-in failed: $errorMsg", task.exception)
                }
            }
    }
}