package com.example.citypulseai

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.citypulseai.databinding.DialogChangePictureBinding

/**
 * Dialog Fragment for selecting options to change or delete a profile picture.
 */
class ChangePictureDialogFragment : DialogFragment() {

    private var _binding: DialogChangePictureBinding? = null
    private val binding get() = _binding!!

    // Launcher to handle picking an image from the gallery
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            // Send the selected image URI back to the EditProfileFragment
            setFragmentResult("request_key", bundleOf("image_uri" to uri.toString()))
            dismiss()
        }
    }

    // Launcher to handle taking a photo with the camera
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            // Note: TakePicturePreview returns a small bitmap. For a full-size image,
            // you'd need to use a different contract and save the file.
            // For a simple example, we'll convert the bitmap to a dummy URI.
            val mockUri = "android.resource://com.example.citypulseai/drawable/new_profile_picture" // Replace with real URI
            setFragmentResult("request_key", bundleOf("image_uri" to mockUri))
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogChangePictureBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.optionTakePhoto.setOnClickListener {
            // Launch the camera app
            takePictureLauncher.launch(null)
        }

        binding.optionChooseFile.setOnClickListener {
            // Launch the gallery/file picker, filtering for images
            pickImageLauncher.launch("image/*")
        }

        binding.optionDeletePhoto.setOnClickListener {
            // Send a result to tell the EditProfileFragment to delete the image
            setFragmentResult("request_key", bundleOf("is_deleted" to true))
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}