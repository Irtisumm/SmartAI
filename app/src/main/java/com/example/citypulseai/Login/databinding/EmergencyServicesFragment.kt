package com.example.citypulseai

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.citypulseai.databinding.FragmentEmergencyServicesBinding

class EmergencyServicesFragment : Fragment() {

    private var _binding: FragmentEmergencyServicesBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): EmergencyServicesFragment = EmergencyServicesFragment()
        private const val EMERGENCY_NUMBER = "999" // Universal emergency number example
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Header Navigation
        binding.cvBackArrowContainer.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. SOS Button
        binding.cvSosButton.setOnClickListener {
            Toast.makeText(context, "SOS Alert Activated. Calling $EMERGENCY_NUMBER.", Toast.LENGTH_LONG).show()
            dialNumber(EMERGENCY_NUMBER)
        }

        // 3. Direct Call Actions
        binding.cvCallPolice.setOnClickListener {
            dialService("Police", EMERGENCY_NUMBER)
        }

        binding.cvCallAmbulance.setOnClickListener {
            dialService("Ambulance", EMERGENCY_NUMBER)
        }

        // 4. Hospital Navigation Actions
        binding.rlHospital1.setOnClickListener {
            navigateToHospital("Hospital Cyberjaya")
        }

        binding.rlHospital2.setOnClickListener {
            navigateToHospital("Putrajaya Hospital")
        }
    }

    private fun dialNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open dialer. Please call $number manually.", Toast.LENGTH_LONG).show()
        }
    }

    private fun dialService(serviceName: String, number: String) {
        Toast.makeText(context, "Initiating call to $serviceName.", Toast.LENGTH_SHORT).show()
        dialNumber(number)
    }

    private fun navigateToHospital(hospitalName: String) {
        val geoUri = Uri.parse("geo:0,0?q=$hospitalName")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        try {
            startActivity(mapIntent)
        } catch (e: Exception) {
            Toast.makeText(context, "Could not open Maps for navigation.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}