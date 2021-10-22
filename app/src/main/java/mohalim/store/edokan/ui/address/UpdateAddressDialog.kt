package mohalim.store.edokan.ui.address

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.*
import mohalim.store.edokan.R

import com.google.firebase.auth.FirebaseAuth
import mohalim.store.edokan.core.model.address.AddressNetwork
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.DialogUpdateAddressBinding
import mohalim.store.edokan.ui.extra.LoadingDialog
import java.util.*


class UpdateAddressDialog : DialogFragment() {


    private lateinit var binding : DialogUpdateAddressBinding;
    private lateinit var loadingDialog: LoadingDialog;

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(activity) }

    private lateinit var addressActivity: AddressActivity
    private lateinit var currentAddress : AddressNetwork

    private var errors = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_update_address, container, false)
        binding.address = currentAddress

        loadingDialog = LoadingDialog()
        addressActivity = activity as AddressActivity

        binding.cityEt.setText(preferenceHelper.getCityName())
        binding.cityEt.isEnabled = false

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
        if(!loadingDialog.isAdded) loadingDialog.show(requireActivity().supportFragmentManager, "")
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
        binding.updateBtn.setOnClickListener {
            validateForm()
            if (errors != 0) return@setOnClickListener
            firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                addressActivity.viewModel.updateAddress(binding.address, it.token.toString())
            }


        }
    }

    /**
     * Validate the form fields
     */
    private fun validateForm() {
        if (binding.addressNameEt.text.isEmpty() || binding.addressNameEt.text.length < 6){
            binding.addressNameEt.error = "Please enter correct address name"
            errors++
        }

        if (binding.addressLine1Et.text.isEmpty() || binding.addressLine1Et.text.length < 6){
            binding.addressLine1Et.error = "Please enter correct address line"
            errors++
        }

        if (binding.addressLine2Et.text.isEmpty() || binding.addressLine2Et.text.length < 6){
            binding.addressLine2Et.error = "Please enter correct address line"
            errors++
        }

        if (binding.cityEt.text.isEmpty()){
            binding.cityEt.error = "Please enter correct city"
            errors++
        }

    }

    fun setAddress(address: AddressNetwork) {
        this.currentAddress = address
    }

}