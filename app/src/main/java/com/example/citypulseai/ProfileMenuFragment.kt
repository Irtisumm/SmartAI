package com.example.citypulseai

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.citypulseai.databinding.FragmentProfileMenuBinding

class ProfileMenuFragment : Fragment() {
    private var _binding: FragmentProfileMenuBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): ProfileMenuFragment = ProfileMenuFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Disable unimplemented features
        binding.rowLanguages.isEnabled = false
        binding.rowHelpSupport.isEnabled = false

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.action_profileMenuFragment_to_editProfileFragment)
        }

        binding.rowChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_profileMenuFragment_to_changePasswordFragment)
        }

        binding.rowChangePin.setOnClickListener {
            findNavController().navigate(R.id.action_profileMenuFragment_to_changePinFragment)
        }

        binding.rowNotification.setOnClickListener {
            findNavController().navigate(R.id.action_profileMenuFragment_to_notificationSettingsFragment)
        }

        binding.rowLanguages.setOnClickListener {
            Toast.makeText(requireContext(), "Languages clicked", Toast.LENGTH_SHORT).show()
        }

        binding.rowHelpSupport.setOnClickListener {
            Toast.makeText(requireContext(), "Help & Support clicked", Toast.LENGTH_SHORT).show()
        }

        binding.tvLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}