package com.example.citypulseai

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.citypulseai.databinding.FragmentNotificationSettingsBinding
import android.view.InflateException

class NotificationSettingsFragment : Fragment() {
    private var _binding: FragmentNotificationSettingsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): NotificationSettingsFragment = NotificationSettingsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return try {
            _binding = FragmentNotificationSettingsBinding.inflate(inflater, container, false)
            binding.root
        } catch (e: InflateException) {
            Log.e("NotificationSettingsFragment", "Inflation failed", e)
            // Fallback: Inflate raw XML or show error layout
            inflater.inflate(R.layout.fragment_notification_settings, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackNotificationSettings.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnSaveNotificationSettings.setOnClickListener {
            val general = binding.switchGeneralNotification.isChecked
            val sound = binding.switchSound.isChecked
            val updates = binding.switchAppUpdates.isChecked

            Toast.makeText(requireContext(), "Settings saved: General=$general, Sound=$sound, Updates=$updates", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}