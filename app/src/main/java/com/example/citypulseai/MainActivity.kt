package com.example.citypulseai

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.citypulseai.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Navigation Controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        if (navHostFragment == null) {
            Log.e("MainActivity", "NavHostFragment not found for ID R.id.nav_host_fragment")
            finish()
            return
        }
        navController = navHostFragment.navController

        setupBottomNav()
    }

    private fun setupBottomNav() {
        val navMap = mapOf(
            binding.navHome.id to R.id.homeFragment,
            binding.navInsights.id to R.id.insightsFragment,
            binding.navAnalytics.id to R.id.analyticsFragment,
            binding.navProfile.id to R.id.profileMenuFragment
        )

        val darkGray = ContextCompat.getColor(this, android.R.color.darker_gray)
        val vividBlue = ContextCompat.getColor(this, R.color.vividBlue)

        // Handle standard tab clicks
        navMap.forEach { (viewId, destId) ->
            findViewById<android.view.View>(viewId)?.setOnClickListener {
                val current = navController.currentDestination?.id
                if (current != destId) {
                    try {
                        navController.navigate(destId)
                    } catch (e: Exception) {
                        Log.w("MainActivity", "Navigation to $destId failed", e)
                    }
                }
                updateSelection(viewId, vividBlue, darkGray)
            } ?: Log.w("MainActivity", "View with ID $viewId not found")
        }

        // Handle FAB click
        binding.fabCenter.setOnClickListener {
            val destId = R.id.chatbotFragment
            val current = navController.currentDestination?.id
            if (current != destId) {
                try {
                    navController.navigate(destId)
                } catch (e: Exception) {
                    Log.w("MainActivity", "Navigation to $destId failed", e)
                }
            }
            updateSelection(binding.fabCenter.id, vividBlue, darkGray)
        }

        // Update selection on destination change
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val selectedNavId = when (destination.id) {
                R.id.homeFragment -> binding.navHome.id
                R.id.insightsFragment -> binding.navInsights.id
                R.id.analyticsFragment -> binding.navAnalytics.id
                R.id.profileMenuFragment -> binding.navProfile.id
                R.id.chatbotFragment -> binding.fabCenter.id
                else -> -1
            }
            if (selectedNavId != -1) {
                updateSelection(selectedNavId, vividBlue, darkGray)
            } else {
                Log.w("MainActivity", "Unknown destination: ${destination.id}")
            }
        }

        // Set initial selection to Home
        updateSelection(binding.navHome.id, vividBlue, darkGray)
    }

    private fun updateSelection(selectedId: Int, vividBlue: Int, darkGray: Int) {
        val items = listOf(binding.navHome, binding.navInsights, binding.navAnalytics, binding.navProfile)

        // Reset all standard icons to gray
        items.forEach { layout ->
            try {
                val imageView = layout.getChildAt(0) as? ImageView
                imageView?.setColorFilter(darkGray)
            } catch (e: Exception) {
                Log.w("MainActivity", "Failed to reset icon color for layout ${layout.id}", e)
            }
        }

        // Handle FAB (ImageButton) separately
        if (selectedId == binding.fabCenter.id) {
            try {
                binding.fabCenter.setColorFilter(vividBlue)
            } catch (e: Exception) {
                Log.w("MainActivity", "Failed to set FAB tint", e)
            }
            return
        }

        // Highlight the selected standard item
        try {
            val selectedView = findViewById<android.view.View>(selectedId)
            if (selectedView is android.widget.LinearLayout) {
                val imageView = selectedView.getChildAt(0) as? ImageView
                imageView?.setColorFilter(vividBlue) ?: Log.w("MainActivity", "Selected view ${selectedId} has no ImageView")
            }
        } catch (e: Exception) {
            Log.w("MainActivity", "Failed to highlight selected item $selectedId", e)
        }
    }
}