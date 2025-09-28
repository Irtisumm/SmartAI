package com.example.citypulseai

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.citypulseai.databinding.FragmentHomeBinding
import java.util.Calendar


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCardClickListeners()
        setupCategoryClickListeners()
        updateGreeting()

        // Ensure emergency CardView always navigates to EmergencyServicesFragment
        binding.cvEmergency.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_homeFragment_to_emergencyServicesFragment)
            } catch (e: Exception) {
                Log.e("HomeFragment", "Navigation to emergencyServicesFragment failed", e)
                Toast.makeText(context, "Navigation failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCardClickListeners() {
        // 🗺 Full Travel Plan Card
        binding.cvFullTravelPlan.setOnClickListener {
            openChatWithQuery("Create a complete travel plan for Cyberjaya including top attractions, restaurants, and activities for a full day")
        }

        // ⚡ Quick Tips Card
        binding.cvQuickTips.setOnClickListener {
            openChatWithQuery("Give me quick travel tips for visiting Cyberjaya - transportation, best times to visit, and local customs")
        }

        // ⭐ Famous Places Card
        binding.cvFamousPlaces.setOnClickListener {
            openChatWithQuery("What are the most famous places and attractions in Cyberjaya? Include MMU, mosques, parks, and landmarks")
        }

        // 🆘 Emergency Card - Navigate to EmergencyServicesFragment
        binding.llEmergencyCard.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_homeFragment_to_emergencyServicesFragment)
            } catch (e: Exception) {
                Log.e("HomeFragment", "Navigation to emergencyServicesFragment failed", e)
                Toast.makeText(context, "Navigation failed", Toast.LENGTH_SHORT).show()
            }
        }
        // 🆘 Emergency CardView - Also navigate to EmergencyServicesFragment
        binding.cvEmergency.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_homeFragment_to_emergencyServicesFragment)
            } catch (e: Exception) {
                Log.e("HomeFragment", "Navigation to emergencyServicesFragment failed", e)
                Toast.makeText(context, "Navigation failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCategoryClickListeners() {
        val categories = mapOf(
            "Restaurants" to "Best restaurants and food places in Cyberjaya with locations and specialties",
            "Shopping" to "Shopping malls and centers in Cyberjaya - D'Pulze, IOI City Mall, and local shops",
            "Parks" to "Parks and recreational areas in Cyberjaya for relaxation and outdoor activities",
            "Hotels" to "Hotels and accommodation options in Cyberjaya with prices and amenities",
            "Attractions" to "Tourist attractions and things to do in Cyberjaya",
            "Cafes" to "Best cafes and coffee shops in Cyberjaya for work and meetings",
            "ATMs" to "ATM locations and banking services available in Cyberjaya"
        )

        val categoriesContainer = binding.llCategoryItems
        for (i in 0 until categoriesContainer.childCount) {
            val categoryView = categoriesContainer.getChildAt(i) as? TextView
            categoryView?.let {
                val categoryText = it.text.toString()
                it.setOnClickListener {
                    categories[categoryText]?.let { query ->
                        openChatWithQuery(query)
                    } ?: Log.w("HomeFragment", "No query found for category: $categoryText")
                }
            } ?: Log.w("HomeFragment", "Category view at index $i is not a TextView")
        }
    }

    private fun openChatWithQuery(query: String) {
        try {
            findNavController().navigate(
                R.id.action_homeFragment_to_chatbotFragment,
                Bundle().apply {
                    putString("QUICK_QUERY", query)
                    putBoolean("AUTO_SEND", true)
                }
            )
        } catch (e: Exception) {
            Log.e("HomeFragment", "Navigation to chatbotFragment failed", e)
            Toast.makeText(context, "Navigation failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateGreeting() {
        val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..11 -> "Good Morning!"
            in 12..17 -> "Good Afternoon!"
            else -> "Good Evening!"
        }
        binding.tvGreeting.text = greeting
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}