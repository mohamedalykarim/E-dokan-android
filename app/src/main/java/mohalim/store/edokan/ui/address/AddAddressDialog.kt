package mohalim.store.edokan.ui.address

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
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
import android.location.Geocoder
import android.renderscript.ScriptGroup

import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import mohalim.store.edokan.core.model.address.AddressNetwork
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.ui.extra.LoadingDialog
import java.util.*


class AddAddressDialog : DialogFragment() {

    private var addressLat: Double = 0.0
    private var addressLng: Double = 0.0

    val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34


    private lateinit var binding : DialogAddAddressBinding;
    private lateinit var loadingDialog: LoadingDialog;


    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var mSettingClient : SettingsClient
    private lateinit var mLocationSettingsRequest : LocationSettingsRequest;

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(activity) }
    private lateinit var addressActivity: AddressActivity

    private var errors = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_address, container, false)

        loadingDialog = LoadingDialog()
        addressActivity = activity as AddressActivity

        binding.cityEt.setText(preferenceHelper.getCityName())
        binding.cityEt.isEnabled = false

        initLocationVariables()
        checkIfLocationPermissionGranted()
        clicks()



        return binding.root

    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT,MATCH_PARENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            addressActivity.viewModel.getAddressForUser(
                preferenceHelper.getUserId() + "",
                it.token + ""
            );
        }
    }

    /**
     * Show the loading view
     */
    fun showLoading(){
        if(!loadingDialog.isAdded)
            loadingDialog.show(requireActivity().supportFragmentManager, "")
    }

    /**
     * Hide the loading view
     */
    fun hideLoading(){
        loadingDialog.dismiss()
    }


    private fun clicks() {
        /**
         * handle add button click
         */
        binding.addBtn.setOnClickListener {
            validateForm()
            if (errors != 0) return@setOnClickListener

            val address = preferenceHelper.getUserId()?.let { userId ->
                preferenceHelper.getCityId()?.let { city_id ->
                    AddressNetwork(
                        0,
                        userId,
                        binding.addressNameEt.text.toString(),
                        binding.addressLine1Et.text.toString(),
                        binding.addressLine2Et.text.toString(),
                        city_id,
                        binding.cityEt.text.toString(),
                        addressLat,
                        addressLng
                    )
                }
            }

            addressActivity.addAddress(address!!, binding.switch1.isChecked)

        }
    }

    /**
     * Validate the form fields
     */
    private fun validateForm() {
        if (binding.addressNameEt.text.isEmpty() || binding.addressNameEt.text.length < 6){
            binding.addressNameEt.setError("Please enter correct address name")
            errors++
        }

        if (binding.addressLine1Et.text.isEmpty() || binding.addressLine1Et.text.length < 6){
            binding.addressLine1Et.setError("Please enter correct address line")
            errors++
        }

        if (binding.addressLine2Et.text.isEmpty() || binding.addressLine2Et.text.length < 6){
            binding.addressLine2Et.setError("Please enter correct address line")
            errors++
        }

        if (binding.cityEt.text.isEmpty()){
            binding.cityEt.setError("Please enter correct city")
            errors++
        }

    }


    /**
     * Init location variables used in demand permission of location and update the current location
     */
    private fun initLocationVariables() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                hideLoading()
                locationResult ?: return
                var index = 0
                for (location in locationResult.locations){
                    if (index != 0) return
                    binding.latitudeTV.text = String.format("%.2f", location.latitude)
                    binding.longitudeTV.text = String.format("%.2f", location.longitude)
                    addressLat = location.latitude
                    addressLng = location.longitude
                    index++
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
    }

    /**
     * Check if the permission of the location is granted
     */
    private fun checkIfLocationPermissionGranted() {
        activity?.let {
            if (foregroundPermissionApproved(it)){
                // permission approved
                updateLocation()
            }else{
                // ask for permission
                requestForegroundPermissions(it)
            }
        }
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

    /**
     * Start get location updates
     */
    @SuppressLint("MissingPermission")
    fun updateLocation() {

        if ((activity?.getSystemService(Context.LOCATION_SERVICE ) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)){
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
            showLoading()
        }else{

            /**
             * Start the GPS Service
             */
            mSettingClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener {
                    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                    showLoading()

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

    fun emptyForm() {
        binding.addressNameEt.setText("")
        binding.addressLine1Et.setText("")
        binding.addressLine2Et.setText("")
    }


}