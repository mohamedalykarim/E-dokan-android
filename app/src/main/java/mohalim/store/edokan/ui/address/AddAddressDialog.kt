package mohalim.store.edokan.ui.address

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import mohalim.store.edokan.R
import mohalim.store.edokan.databinding.DialogAddAddressBinding
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

import android.content.IntentSender
import android.content.IntentSender.SendIntentException

import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.common.api.ApiException







class AddAddressDialog : DialogFragment() {

    val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34


    private lateinit var binding : DialogAddAddressBinding;


    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var mSettingClient : SettingsClient
    private lateinit var mLocationSettingsRequest : LocationSettingsRequest;



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_address, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    Log.d("TAG", "onLocationResult: "+ location.latitude)
                }
            }
        }

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        mSettingClient = LocationServices.getSettingsClient(context)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()



        activity?.let {
            if (foregroundPermissionApproved(it)){
                // permission approved
                updateLocation()
            }else{
                // ask for permission
                requestForegroundPermissions(it)
            }
        }



        return binding.root

    }



    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT,MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }


    /**
     * Request Permission from the user
     */
    private fun requestForegroundPermissions(context: Context) {

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        )

    }


    /**
     * Check if foreground permission approved
     */

    private fun foregroundPermissionApproved(context: Context) : Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun showLocationPermissionSnackBar() {
        Snackbar.make(
            binding.root,
            "You must grant location permission to add new address",
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction("Grant") {
                // Request permission
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION),
                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                )
            }
            .show()
    }

    @SuppressLint("MissingPermission")
    fun updateLocation() {
        if ((activity?.getSystemService(Context.LOCATION_SERVICE ) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)){
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }else{

            /**
             * Start the GPS Service
             */
            mSettingClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                }.addOnFailureListener {
                    val statusCode = (it as ApiException).statusCode
                    when (statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val resolvableApiException = it as ResolvableApiException
                                resolvableApiException.startResolutionForResult(
                                    context as Activity,
                                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
                                )
                            } catch (sie: SendIntentException) {
                            Log.i("TAG", "PendingIntent unable to execute request.")
                            }
                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                val errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings."
                                Log.e("TAG", errorMessage)
                            }
                    }
                }
        }


    }


}