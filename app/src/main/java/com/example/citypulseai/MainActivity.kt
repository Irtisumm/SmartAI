package com.example.citypulseai

import android.os.Bundle
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

        // Setup Navigation Controller
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup Custom Bottom Navigation
        setupBottomNav()
    }

    private fun setupBottomNav() {
        // Map layout IDs to destination IDs in nav_graph.xml
        val navMap = mapOf(
            binding.navHome.id to R.id.homeFragment,
            binding.navInsights.id to R.id.insightsFragment,
            binding.navAnalytics.id to R.id.analyticsFragment,
            binding.navProfile.id to R.id.profileMenuFragment
        )

        // Define colors
        val darkGray = ContextCompat.getColor(this, android.R.color.darker_gray)
        val vividBlue = ContextCompat.getColor(this, R.color.vividBlue)

        // Handle standard tab clicks and navigation
        navMap.forEach { (viewId, destId) ->
            findViewById<android.view.View>(viewId).setOnClickListener {
                try {
                    navController.navigate(destId)
                    // The updateSelection function handles coloring based on the navigated ID
                    updateSelection(it.id, vividBlue, darkGray)
                } catch (e: IllegalArgumentException) {
                    // Handle unimplemented fragments gracefully
                    updateSelection(it.id, vividBlue, darkGray)
                }
            }
        }

        // Handle FAB click
        binding.fabCenter.setOnClickListener {
            try {
                navController.navigate(R.id.chatBotFragment)
                // When FAB is clicked, mark the FAB ID as selected
                updateSelection(binding.fabCenter.id, vividBlue, darkGray)
            } catch (e: IllegalArgumentException) {
                updateSelection(binding.fabCenter.id, vividBlue, darkGray)
            }
        }

        // Update selection when fragment changes (e.g., navigating programmatically or via deep link)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val selectedNavId = when (destination.id) {
                R.id.homeFragment -> binding.navHome.id
                R.id.insightsFragment -> binding.navInsights.id
                R.id.analyticsFragment -> binding.navAnalytics.id
                R.id.profileMenuFragment -> binding.navProfile.id
                R.id.chatBotFragment -> binding.fabCenter.id
                else -> -1
            }
            if (selectedNavId != -1) {
                updateSelection(selectedNavId, vividBlue, darkGray)
            }
        }

        // Set initial selection to Home
        updateSelection(binding.navHome.id, vividBlue, darkGray)
    }

    /**
     * Updates the visual appearance of the bottom navigation icons.
     * Note: This function relies on the `activity_main.xml` structure where each nav item
     * (nav_home, nav_insights, etc.) is a LinearLayout containing an ImageView as its first child (index 0).
     */
    private fun updateSelection(selectedId: Int, vividBlue: Int, darkGray: Int) {
        val items = listOf(binding.navHome, binding.navInsights, binding.navAnalytics, binding.navProfile)

        // 1. Reset all standard icons to gray
        items.forEach { layout ->
            val imageView = layout.getChildAt(0) as ImageView
            imageView.setColorFilter(darkGray)
        }

        // 2. If the FAB is selected, we exit after resetting (FAB is styled separately in XML)
        if (selectedId == binding.fabCenter.id) {
            return
        }

        // 3. Highlight the specific selected standard item
        val selectedLayout = items.find { it.id == selectedId }
        selectedLayout?.let { layout ->
            (layout.getChildAt(0) as ImageView).setColorFilter(vividBlue)
        }
    }
}
