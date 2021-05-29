package com.leehendryp.coordinatesapp.presentation

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.icu.util.TimeUnit
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.leehendryp.coordinatesapp.CoordinatesApp
import com.leehendryp.coordinatesapp.R
import com.leehendryp.coordinatesapp.data.local.Coordinates
import com.leehendryp.coordinatesapp.databinding.ActivityMainBinding
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: CoordinatesViewModel

    private val adapter by lazy { CoordinatesListAdapter() }

    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy { createLocationRequest() }
    private val locationCallback by lazy { createLocationCallback() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        injectDependencies()
        initViewModel()
        initRecyclerView()
        observeStateChanges()

        startLocationUpdates()
        binding.buttonManualActivation.setOnClickListener { startLocationUpdates() }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CoordinatesViewModel::class.java)
    }

    private fun initRecyclerView() {
        binding.listLocations.adapter = adapter
        binding.listLocations.layoutManager = LinearLayoutManager(this)
    }

    private fun observeStateChanges() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is State.UpdatedList -> adapter.submitList(state.list)
                State.Saving -> toast(R.string.toast_saving)
                State.NewLocationSaved -> toast(R.string.toast_saved)
                State.Error -> toast(R.string.toast_error)
                else -> Unit
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults[0] == PERMISSION_GRANTED) startLocationUpdates()
            else toast(R.string.toast_permission_rationale)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        if (isFineLocationGranted()) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            requestPermissions(arrayOf(ACCESS_FINE_LOCATION), REQUEST_FINE_LOCATION)
        }
    }

    private fun isFineLocationGranted() =
        checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED

    private fun createLocationRequest() = LocationRequest.create().apply {
        interval = SECONDS.toMillis(2)
        fastestInterval = SECONDS.toMillis(1)
        maxWaitTime = SECONDS.toMillis(5)
        priority = PRIORITY_HIGH_ACCURACY
    }

    private fun createLocationCallback() = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation

            Toast.makeText(
                this@MainActivity,
                location.toText(),
                LENGTH_LONG
            ).show()

            viewModel.dispatch(
                Action.SaveUpdate(
                    Coordinates(
                        timeStamp = Date().toString(),
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                )
            )
        }
    }

    private fun toast(@StringRes string: Int) {
        Toast.makeText(this, getString(string), LENGTH_SHORT).show()
    }

    private fun Location?.toText(): String {
        return if (this != null) "($latitude, $longitude)" else "Unknown location"
    }

    private fun injectDependencies() = (application as CoordinatesApp).appComponent.inject(this)

    companion object {
        private const val REQUEST_FINE_LOCATION = 13
    }
}