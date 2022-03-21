package se.umu.olho0018.mapmarker.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import se.umu.olho0018.mapmarker.MainActivity
import se.umu.olho0018.mapmarker.R
import se.umu.olho0018.mapmarker.viewmodels.MapsViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Observer
import se.umu.olho0018.mapmarker.models.Post

private const val TAG = "MapsFragment"
private const val PREFS_NAME = "FirstAppLaunch"
private const val DEFAULT_ZOOM = 10F
private val DEFAULT_LOCATION = LatLng(0.0, 0.0)

/**
 * @author Oliver Högberg, olho0018
 * Maps fragment to handle the map view and corresponding functionality
 */
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private val mapsViewModel: MapsViewModel by lazy {
        ViewModelProvider(this).get(MapsViewModel::class.java)
    }
    private var map: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var permissionsGranted = false
    // Start activity with contract to check location permissions
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, true) -> {
                permissionsGranted = true
                checkFirstTimeAppUse()      // Display welcome message on first time use
                updateMapUI()               // Update map UI
                moveCameraLastLocation()    // Move map camera to current location
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, true) -> {
                permissionsGranted = true
                checkFirstTimeAppUse()      // Display welcome message on first time use
                updateMapUI()               // Update map UI
                moveCameraLastLocation()    // Move map camera to current location
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        (activity as MainActivity?)?.showTabs()     // Show navigation tabs
        setHasOptionsMenu(true)                     // Setup action bar for creating post

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        // Setup of maps fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.create_post_action_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar to create new post
        return when (item.itemId) {
            R.id.create_post_item -> {
                createPost()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onMapReady(map: GoogleMap) {
        this.map = map
        getPermission()             // Get location permissions
        updateMapUI()               // Update map UI
        moveCameraLastLocation()    // Move map camera to current location

        // Add custom markers from posts in database to map
        mapsViewModel.posts.observe(
            viewLifecycleOwner,
            Observer { posts ->
                for (post in posts) {
                    addMarkerToMap(map, post)
                }
            }
        )

        // Listen to click events from map information window
        map.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker) {
        // Listen to click events from map information window
        mapsViewModel.post(marker.tag as UUID).observe(
            viewLifecycleOwner,
            Observer { post ->
                findNavController().navigate(
                    MapsFragmentDirections.actionMapsFragmentToPostDetailFragment(
                        post,
                        MapsFragment::class.simpleName as String
                    )
                )
            }
        )
    }

    /**
     * Gets location permissions
     */
    private fun getPermission() {

        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Launch activity to ask for location permissions
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }
        else {
            permissionsGranted = true
        }
    }

    /**
     * Moves the map camera to current location
     */
    @SuppressLint("MissingPermission")
    private fun moveCameraLastLocation() {

        if (permissionsGranted) {

            /* Get the last known location
             * (good approximate for current location)
             * and listen to location changes
             */
            val locationResult = fusedLocationClient.lastLocation
            locationResult.addOnCompleteListener(this.requireActivity()) { task ->

                if (task.isSuccessful) {

                    if (task.result != null) {
                        // Move camera to current location
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                task.result!!.latitude,
                                task.result!!.longitude),
                            DEFAULT_ZOOM
                        ))
                    }
                }
                else {
                    // Move camera to default location if current location can't be found
                    map?.moveCamera(
                        CameraUpdateFactory
                        .newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM))
                    map?.uiSettings?.isMyLocationButtonEnabled = false
                }
            }
        }
    }

    /**
     * Updates the map UI
     */
    @SuppressLint("MissingPermission")
    private fun updateMapUI() {

        if (map == null) {
            return
        }

        if (permissionsGranted) {
            map?.isMyLocationEnabled = true
            map?.uiSettings?.isMyLocationButtonEnabled = true
        }
        else {
            map?.isMyLocationEnabled = false
            map?.uiSettings?.isMyLocationButtonEnabled = false
        }
    }

    /**
     * Creates new post by getting current location
     * and sending user to PostCreateFragment
     */
    @SuppressLint("MissingPermission")
    private fun createPost() {

        if (permissionsGranted) {

            val locationResult = fusedLocationClient.lastLocation
            locationResult.addOnCompleteListener(this.requireActivity()) { task ->

                if (task.isSuccessful) {

                    if (task.result != null) {
                        val latLng = LatLng(
                            task.result!!.latitude,
                            task.result!!.longitude)
                        findNavController().navigate(
                            MapsFragmentDirections.actionMapsFragmentToPostCreateFragment(latLng))
                    }
                }
            }
        }
    }

    /**
     * Adds custom markers to map with
     * corresponding post attributes
     *
     * @param map GoogleMap to add markers too
     * @param post Specific post to use for marker attributes
     */
    private fun addMarkerToMap(map: GoogleMap, post: Post) {

        val latLng = LatLng(post.latitude,post.longitude)
        // Get color in FloatArray format from Hex String
        val color = mapsViewModel.convertHexToHue("#${post.categoryColor}")?.get(0)

        // Custom marker with added tag with post UUID
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(post.title)
                .snippet(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.postDate))
                .icon(color?.let { BitmapDescriptorFactory.defaultMarker(it) })
        )?.tag = post.id
    }

    /**
     * Checks if it is the first time a user
     * is using the application
     */
    private fun checkFirstTimeAppUse() {

        // Get shared preferences
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.apply {
            // Check if first time app is used
            if (!getBoolean(PREFS_NAME, false)) {
                sharedPreferences.edit().apply {
                    putBoolean(PREFS_NAME, true) // Mark app as used
                    apply()
                }
                welcomeDialog() // Create a welcome dialog
            }
        }
    }

    /**
     * Creates a welcome dialog that takes user to create first post
     */
    private fun welcomeDialog() {

        AlertDialog.Builder(context)
            .setTitle("Welcome to MapMarker")
            .setMessage("Lets create your first post!")
            .setPositiveButton(R.string.confirm_label) { _, _ ->
                createPost()
            }
            .show()
    }
}