package com.example.citypulseai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.citypulseai.databinding.FragmentChangePasswordBinding

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): ChangePasswordFragment = ChangePasswordFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button navigation
        binding.btnBackPassword.setOnClickListener {
            findNavController().popBackStack()
        }

        // Password validation logic
        binding.btnSubmitPassword.setOnClickListener {
            val newPassword = binding.etNewPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            // Validate password length (at least 8 characters)
            val isLengthValid = newPassword.length >= 8
            // Validate unique code (e.g., contains @, !, or #)
            val isUniqueValid = newPassword.contains(Regex("[@!#]"))

            // Update validation UI
            binding.ivCheckLength.setImageResource(
                if (isLengthValid) R.drawable.ic_check_circle else R.drawable.ic_error
            )
            binding.ivCheckLength.setColorFilter(
                if (isLengthValid) 0xFF00C566.toInt() else 0xFFFF0000.toInt()
            )
            binding.ivCheckUnique.setImageResource(
                if (isUniqueValid) R.drawable.ic_check_circle else R.drawable.ic_error
            )
            binding.ivCheckUnique.setColorFilter(
                if (isUniqueValid) 0xFF00C566.toInt() else 0xFFFF0000.toInt()
            )

            // Validate inputs
            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else if (newPassword != confirmPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else if (!isLengthValid || !isUniqueValid) {
                Toast.makeText(context, "Password does not meet requirements", Toast.LENGTH_SHORT).show()
            } else {
                // Handle password change logic (e.g., update backend)
                Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}