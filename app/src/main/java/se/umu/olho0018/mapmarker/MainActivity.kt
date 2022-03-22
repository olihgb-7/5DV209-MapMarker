package se.umu.olho0018.mapmarker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayout


private const val TAG = "MainActivity"
private const val CURRENT_TAB_KEY = "currentTab"

/**
 * @author Oliver Högberg, olho0018
 * Main activity with that handles the different fragments and their corresponding navigation.
 * Application has been tested on emulated Nexus 5-API 24, Nexus 5-API 28 and Pixel 4-API 30.
 *
 * NOTE:    Depending on the emulator used it might take awhile for the map to load.
 *          Nexus 5-API 28 seems to have worked well in both testing and installing .apk
 *          Pixel 4-API 30 works well besides loading time for the map
 *          Nexus 5-API 24 works well for testing, but for some reason it refuses to install the .apk
 */
class MainActivity : AppCompatActivity() {

    private var tabsInFocus = true

    private lateinit var tabLayout: TabLayout
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup of nav controller
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Setup and handling of tabs and their events
        tabLayout = findViewById(R.id.tab_layout)

        // NOTE: Placement of this if statement is crucial for correctly recovering the instance state!
        if (savedInstanceState != null) {
            if (savedInstanceState.getInt(CURRENT_TAB_KEY) != 0) {
                tabLayout.getTabAt(1)?.select()
            }
        }

        tabsSetup(navController)
    }

    /**
     * Changes the tab position when back button is pressed
     *
     * Implemented with help from:
     * https://stackoverflow.com/questions/53669199/onbackpressed-change-tabs-in-android
     */
    override fun onBackPressed() {

        if (tabsInFocus && tabLayout.selectedTabPosition != 0) {
            tabLayout.getTabAt(0)?.select()
        }
        else {
            super.onBackPressed()
        }
    }

    /**
     * Setup of tabs and their events
     *
     * @param navController Takes the NavController to be used in fragment navigation of the application
     */
    private fun tabsSetup(navController: NavController) {

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    if (tab.position == 0) {
                        navController.navigate(R.id.action_postListFragment_to_mapsFragment)
                    }
                    else if (tab.position == 1) {
                        navController.navigate(R.id.action_mapsFragment_to_postListFragment)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    /**
     * Public method to hide activity tabs from specific fragments
     */
    fun hideTabs() {
        tabLayout.visibility = View.GONE
        tabsInFocus = false
    }

    /**
     * Public method to show activity tabs in specific fragments
     */
    fun showTabs() {
        tabLayout.visibility = View.VISIBLE
        tabsInFocus = true
    }

    /**
     * Handle instance state saving of correct tabs selection
     */
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(CURRENT_TAB_KEY, tabLayout.selectedTabPosition)
    }
}