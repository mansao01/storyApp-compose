@file:Suppress("DEPRECATION")

package com.example.mystoryappcompose

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.mystoryappcompose.data.local.model.LocationModel
import com.example.mystoryappcompose.preferences.AuthViewModel
import com.example.mystoryappcompose.ui.MyStoryApp
import com.example.mystoryappcompose.ui.theme.MyStoryAppComposeTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val authViewModel by viewModels<AuthViewModel> { AuthViewModel.Factory }
    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var locationRequired = false
    private var locationModel by mutableStateOf(
        LocationModel(0.0, 0.0)
    )
    private var isLocationEnabled by mutableStateOf(false)
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            installSplashScreen().setKeepOnScreenCondition { !authViewModel.isLoading.value }
            MyStoryAppComposeTheme {
                // A surface container using the 'background' color from the theme
                val context = LocalContext.current
                SetupLocationServices(context)

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination = authViewModel.startDestination

                    MyStoryApp(startDestination = startDestination.value, location = locationModel, locationEnabled = isLocationEnabled)
                }
            }
        }
    }

    @Composable
    private fun SetupLocationServices(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = createLocationCallback()

        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (hasLocationPermissions(context, permissions)) {
            startLocationUpdates()
            isLocationEnabled = true

        } else {
            RequestLocationPermissions(context, permissions)
        }
    }

    private fun createLocationCallback(): LocationCallback {
        return object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.firstOrNull()?.let { location ->
                    // Update UI with location data
                    val updatedLocation = getLocationDetails(location.latitude, location.longitude)
                    locationModel = updatedLocation
                }
            }
        }
    }

    private fun hasLocationPermissions(context: Context, permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    @Composable
    private fun RequestLocationPermissions(context: Context, permissions: Array<String>) {
        val launcherMultiplePermissions = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissionsMap ->
            val areGranted = permissionsMap.values.all { it }
            if (areGranted) {
                locationRequired = true
                startLocationUpdates()
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

        LaunchedEffect(Unit) {
            launcherMultiplePermissions.launch(permissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationCallback?.let {
            val locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                it,
                Looper.getMainLooper()
            )
        }
    }

    private fun getLocationDetails(latitude: Double, longitude: Double): LocationModel {
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            return if (addresses!!.isNotEmpty()) {

                LocationModel(
                    latitude,
                    longitude,

                    )
            } else {
                LocationModel(latitude, longitude)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return LocationModel(
                latitude,
                longitude,
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
    }
}
