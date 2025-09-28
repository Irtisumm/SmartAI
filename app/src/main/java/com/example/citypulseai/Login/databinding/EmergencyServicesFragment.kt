package com.example.citypulseai

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.citypulseai.databinding.FragmentEmergencyServicesBinding

class EmergencyServicesFragment : Fragment() {

    private var _binding: FragmentEmergencyServicesBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): EmergencyServicesFragment = EmergencyServicesFragment()
        private const val EMERGENCY_NUMBER_POLICE = "999"
        private const val EMERGENCY_NUMBER_MEDICAL = "999"
        private const val EMERGENCY_NUMBER_FIRE = "994"
        private const val REQUEST_CALL_PERMISSION = 100
        // Hospital coordinates for precise navigation
        private val HOSPITAL_LOCATIONS = mapOf(
            "Hospital Cyberjaya" to "2.9216,101.6552", // Cyberjaya Hospital
            "Putrajaya Hospital" to "2.9294,101.6723"  // Putrajaya Hospital
        )
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
        setupEmergencyButtons()
    }

    private fun setupEmergencyButtons() {
        // Back Navigation
        binding.cvBackArrowContainer.setOnClickListener {
            try {
                parentFragmentManager.popBackStack()
            } catch (e: Exception) {
                Toast.makeText(context, "Navigation failed", Toast.LENGTH_SHORT).show()
            }
        }

        // SOS Button
        binding.cvSosButton.setOnClickListener {
            Toast.makeText(context, "SOS Alert Activated. Calling $EMERGENCY_NUMBER_POLICE.", Toast.LENGTH_LONG).show()
            makeEmergencyCall(EMERGENCY_NUMBER_POLICE, "SOS Emergency")
        }

        // Police Emergency
        binding.cvCallPolice.setOnClickListener {
            showEmergencyDialog(
                "Police Emergency",
                "Call Malaysia Police?",
                EMERGENCY_NUMBER_POLICE
            )
        }

        // Ambulance (Medical) Emergency
        binding.cvCallAmbulance.setOnClickListener {
            showEmergencyDialog(
                "Medical Emergency",
                "Call Malaysia Emergency Medical?",
                EMERGENCY_NUMBER_MEDICAL
            )
        }

        // Additional Emergency Options (Fire, Hospitals, Police Stations)
        binding.cvNearbyHospitals.setOnClickListener {
            showOptionsDialog()
        }

        // Hospital Navigation
        binding.rlHospital1.setOnClickListener {
            navigateToHospital("Hospital Cyberjaya")
        }

        binding.rlHospital2.setOnClickListener {
            navigateToHospital("Putrajaya Hospital")
        }
    }

    private fun showEmergencyDialog(title: String, message: String, number: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("⚠ $title")
            .setMessage(message)
            .setPositiveButton("Call Now") { _, _ ->
                makeEmergencyCall(number, title)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showOptionsDialog() {
        val options = arrayOf("Fire Emergency", "Nearby Hospitals", "Nearby Police Stations")
        AlertDialog.Builder(requireContext())
            .setTitle("Emergency Options")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEmergencyDialog(
                        "Fire Emergency",
                        "Call Malaysia Fire Department?",
                        EMERGENCY_NUMBER_FIRE
                    )
                    1 -> showHospitalsList()
                    2 -> showPoliceStationsList()
                }
            }
            .show()
    }

    private fun makeEmergencyCall(number: String, serviceName: String) {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$number")
            }
            try {
                startActivity(intent)
                Toast.makeText(context, "Initiating call to $serviceName.", Toast.LENGTH_SHORT).show()
            } catch (e: SecurityException) {
                openDialer(number, serviceName)
            }
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CALL_PHONE),
                REQUEST_CALL_PERMISSION
            )
        }
    }

    private fun openDialer(number: String, serviceName: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        try {
            startActivity(dialIntent)
            Toast.makeText(context, "Opening dialer for $serviceName.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "Cannot open dialer. Please call $number manually.", Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateToHospital(hospitalName: String) {
        val coordinates = HOSPITAL_LOCATIONS[hospitalName] ?: "0,0"
        val geoUri = Uri.parse("geo:$coordinates?q=$hospitalName")
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
            setPackage("com.google.android.apps.maps") // Prefer Google Maps
        }
        try {
            startActivity(mapIntent)
            Toast.makeText(context, "Navigating to $hospitalName.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // Fallback to generic geo URI
            val fallbackUri = Uri.parse("geo:0,0?q=$hospitalName")
            val fallbackIntent = Intent(Intent.ACTION_VIEW, fallbackUri)
            try {
                startActivity(fallbackIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "Could not open Maps. Please search for $hospitalName manually.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showHospitalsList() {
        val hospitals = arrayOf(
            "🏥 Cyberjaya University College Medical Centre - 03-8312 7800",
            "🏥 Columbia Asia Hospital - 03-8322 7500",
            "🏥 Putrajaya Hospital - 03-8312 4200",
            "🏥 Serdang Hospital - 03-8947 5555"
        )

        AlertDialog.Builder(requireContext())
            .setTitle("🏥 Nearby Hospitals")
            .setItems(hospitals) { _, which ->
                val hospital = hospitals[which]
                val number = hospital.substringAfter(" - ")
                makeEmergencyCall(number.replace(" ", ""), "Hospital")
            }
            .show()
    }

    private fun showPoliceStationsList() {
        val policeStations = arrayOf(
            "👮 Cyberjaya Police Station - 03-8911 4222",
            "👮 Putrajaya Police Station - 03-8888 5050",
            "👮 Serdang Police Station - 03-8911 3333"
        )

        AlertDialog.Builder(requireContext())
            .setTitle("👮 Nearby Police Stations")
            .setItems(policeStations) { _, which ->
                val station = policeStations[which]
                val number = station.substringAfter(" - ")
                makeEmergencyCall(number.replace(" ", ""), "Police Station")
            }
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Call permission granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Call permission denied. Please use dialer manually.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}