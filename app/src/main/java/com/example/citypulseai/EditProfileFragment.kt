package com.example.citypulseai

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.example.citypulseai.databinding.FragmentEditProfileBinding

/**
 * Fragment for editing the user's personal details and handling profile picture updates.
 */
class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up listener for results from the ChangePictureDialogFragment
        setFragmentResultListener("request_key") { _, bundle ->
            val imageUriString = bundle.getString("image_uri")
            val isDeleted = bundle.getBoolean("is_deleted", false)

            if (imageUriString != null) {
                val imageUri = Uri.parse(imageUriString)
                // Use Glide to load the selected image into the ImageView
                Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.profile_circle) // Show a placeholder while loading
                    .into(binding.imgProfile)
                Toast.makeText(context, "Profile picture updated", Toast.LENGTH_SHORT).show()
            } else if (isDeleted) {
                // Reset the ImageView to the default profile circle
                binding.imgProfile.setImageResource(R.drawable.profile_circle)
                Toast.makeText(context, "Profile picture deleted", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBackEdit.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.imgCameraIcon.setOnClickListener {
            ChangePictureDialogFragment().show(parentFragmentManager, "ChangePictureDialog")
        }

        binding.imgProfile.setOnClickListener {
            ChangePictureDialogFragment().show(parentFragmentManager, "ChangePictureDialog")
        }

        binding.btnSaveChanges.setOnClickListener {
            val fullName = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val address = binding.etAddress.text.toString()

            // Basic Validation
            if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(context, "Profile updated successfully (mocked)", Toast.LENGTH_SHORT).show()

            SuccessDialogFragment.newInstance("Success", "Your profile is successfully updated").show(
                parentFragmentManager, "SuccessDialog"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}