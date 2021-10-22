package mohalim.store.edokan.ui.address

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.address.Address
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.ActivityAddressBinding
import mohalim.store.edokan.databinding.RowAddressBinding
import android.content.Intent
import android.widget.Toast
import mohalim.store.edokan.core.model.address.AddressNetwork
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.ui.extra.LoadingDialog
import mohalim.store.edokan.ui.extra.MessageDialog


@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddressBinding;

    val viewModel : AddressViewModel by viewModels()

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(this) }
    lateinit var addressRVAdpter : AddressRecyclerAdapter;

    private lateinit var addAddressDialog : AddAddressDialog;
    private lateinit var updateAddressDialog: UpdateAddressDialog

    lateinit var loadingDialog: LoadingDialog
    lateinit var messagesDialog : MessageDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address)

        updateAddressDialog = UpdateAddressDialog()
        addAddressDialog = AddAddressDialog();
        messagesDialog = MessageDialog()
        loadingDialog = LoadingDialog()

        click()

        initAddressRV()
        subscribeObservers();

    }

    private fun click() {
        binding.floatingActionButton.setOnClickListener {
            if (addAddressDialog.isAdded) return@setOnClickListener
            addAddressDialog.show(supportFragmentManager, "AddAddressDialog")
        }

    }

    override fun onResume() {
        super.onResume()

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            viewModel.getAddressForUser(
                preferenceHelper.getUserId() + "",
                it.token + ""
            );
        }

    }

    /**
     * Subscribe the observers from Viewmodel
     */
    private fun subscribeObservers() {
        /**
         * Get all addresses observer
         */
        viewModel.addresses.observe(this, Observer {
            when(it){
                is DataState.Loading ->{
                    if (!loadingDialog.isAdded) loadingDialog.show(supportFragmentManager, "LoadingDialog")
                }
                is DataState.Success ->{
                    addressRVAdpter.addresses.clear()
                    addressRVAdpter.addresses.addAll(it.data)
                    addressRVAdpter.notifyDataSetChanged()
                    loadingDialog.dismiss()
                }

                is DataState.Failure->{
                    Log.d("TAG", "subscribeObservers: "+ it.exception.message)
                    loadingDialog.dismiss()
                }
            }
        })

        /**
         * Add address Observer
         */
        viewModel.addAddressObserver.observe(this, {
            when(it){
                is DataState.Loading ->{
                    if (!addAddressDialog.isAdded) return@observe
                    addAddressDialog.showLoading()
                }
                is DataState.Success ->{
                    if (!addAddressDialog.isAdded) return@observe
                    addAddressDialog.hideLoading()
                    addAddressDialog.emptyForm()
                    addAddressDialog.dismiss()

                    firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                        viewModel.updateUserData( it.token + "");
                    }
                }

                is DataState.Failure->{
                    Log.d("TAG", "subscribeObservers: "+ it.exception.message)
                }
            }

        })

        /**
         * Update Address Observer
         */

        viewModel.updateAddressObserver.observe(this, Observer {
            when(it){
                is DataState.Loading ->{
                    if (!loadingDialog.isAdded) loadingDialog.show(supportFragmentManager, "LoadingDialog")
                }

                is DataState.Success ->{
                    if (loadingDialog.isAdded) loadingDialog.dismiss()
                    if (updateAddressDialog.isAdded) updateAddressDialog.dismiss()
                }

                is DataState.Failure ->{
                    Log.d("TAG", "subscribeObservers: "+it.exception.message)
                    if (loadingDialog.isAdded) loadingDialog.dismiss()
                }
            }
        })

        /**
         *  Delete Address Observer
         */
        viewModel.deleteAddressObserver.observe(this, Observer {
            when(it){
                is DataState.Loading ->{
                    if (!loadingDialog.isAdded) loadingDialog.show(supportFragmentManager, "LoadingDialog")
                }
                is DataState.Success ->{

                    firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                        viewModel.getAddressForUser(
                            preferenceHelper.getUserId() + "",
                            it.token + ""
                        );
                    }

                    messagesDialog.dismiss()
                    loadingDialog.dismiss()

                }

                is DataState.Failure->{
                    Log.d("TAG", "subscribeObservers: "+ it.exception.message)
                    messagesDialog.dismiss()
                    loadingDialog.dismiss()
                }
            }
        })

        /**
         * Update Address Observer
         */
        viewModel.updateDataObserver.observe(this, {
            when(it){
                is DataState.Loading ->{
                }
                is DataState.Success ->{
                    addressRVAdpter.notifyDataSetChanged()
                }

                is DataState.Failure->{
                }
            }
        })

        /**
         * Set Address default Observer
         */
        viewModel.setDefaultObserver.observe(this, Observer {
            when(it){
                is DataState.Loading ->{
                    if (!loadingDialog.isAdded) loadingDialog.show(supportFragmentManager, "LoadingDialog")
                }
                is DataState.Success ->{
                    firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                        viewModel.updateUserData( it.token + "");
                    }
                    loadingDialog.dismiss()
                }

                is DataState.Failure->{
                    Log.d("TAG", "subscribeObservers: "+ it.exception.message)
                    loadingDialog.dismiss()
                }
            }
        })
    }

    private fun initAddressRV() {
        addressRVAdpter = AddressRecyclerAdapter(
            viewModel,
            ArrayList(),
            preferenceHelper,
            firebaseAuth,
            messagesDialog,
            updateAddressDialog,
            this@AddressActivity
        )
        val layoutManager = LinearLayoutManager(this@AddressActivity, LinearLayoutManager.VERTICAL, false)
        binding.addressRV.layoutManager = layoutManager
        binding.addressRV.adapter = addressRVAdpter

        val dividerItemDecoration : DividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.addressRV.addItemDecoration(dividerItemDecoration)
    }




    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            addAddressDialog.REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE ->{
                when{
                    resultCode == Activity.RESULT_OK ->{
                        addAddressDialog.updateLocation()
                    }
                }
            }
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            addAddressDialog.REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE ->
                when {
                    grantResults.isEmpty() -> {
                        if (!addAddressDialog.isAdded) return
                        addAddressDialog.showLocationPermissionSnackBar()
                    }

                    grantResults[0] == PackageManager.PERMISSION_GRANTED ->{
                        // Permission was granted.
                        if (!addAddressDialog.isAdded) return
                        addAddressDialog.updateLocation()

                    }
                    else -> {
                        // Permission denied.
                        if (!addAddressDialog.isAdded) return
                        addAddressDialog.showLocationPermissionSnackBar()
                    }
                }


        }
    }

    fun addAddress(address: AddressNetwork, isDefault: Boolean) {
        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            Log.d("TAG", "addAddress: get fToken")
            viewModel.addAddress(address, isDefault, it.token.toString())
        }
    }


    class AddressRecyclerAdapter (
        val viewmodel : AddressViewModel,
        var addresses : MutableList<Address>,
        val prefHelper: IPreferenceHelper,
        val firebaseAuth: FirebaseAuth,
        val messageDialog: MessageDialog,
        val updateAddressDialog: UpdateAddressDialog,
        val addressActivity: AddressActivity

        ) : RecyclerView.Adapter<AddressRecyclerAdapter.AddressViewHolder>() {

        class AddressViewHolder (
            val viewmodel : AddressViewModel,
            val binding : RowAddressBinding,
            val prefHelper: IPreferenceHelper,
            val firebaseAuth: FirebaseAuth,
            val messageDialog: MessageDialog,
            val updateAddressDialog: UpdateAddressDialog,
            val addressActivity: AddressActivity

            ) : RecyclerView.ViewHolder(binding.root) {

            fun bindItem(address: AddressNetwork, messageDialog: MessageDialog){
                binding.addressTitleTv.text = address.addressName
                binding.addressLine1Tv.text = address.addressLine1
                binding.addressLine2Tv.text = address.addressLine2 + " "+ address.city_name

                if (address.addressId == prefHelper.getDefaultAddressId()){
                    binding.defaultTv.visibility = View.VISIBLE
                    binding.setDefaultBtn.visibility = View.GONE
                }else{
                    binding.defaultTv.visibility = View.GONE
                    binding.setDefaultBtn.visibility = View.VISIBLE
                }

                binding.editBtn.setOnClickListener {
                    if (!updateAddressDialog.isAdded)
                        updateAddressDialog.setAddress(address)
                        updateAddressDialog.show(addressActivity.supportFragmentManager, "UpdateAddressDialog")
                }

                binding.setDefaultBtn.setOnClickListener {
                    firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                        viewmodel.setDefault(address.addressId, it.token.toString())
                    }
                }

                binding.deleteBtn.setOnClickListener {
                    if (address.addressId == prefHelper.getDefaultAddressId()) {
                        Toast.makeText(addressActivity, "You can not delete default address", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    messageDialog.setStyle(Constants.constants.MESSAGE_DIALOG_DELETE_ADDRESS_STYLE)
                    messageDialog.addressActivity = addressActivity
                    messageDialog.addressId = address.addressId

                    if(!messageDialog.isAdded){
                        messageDialog.show(addressActivity.supportFragmentManager, "MessageDialog")
                    }
                }

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
            val binding : RowAddressBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_address,
                parent,
                false
            )

            return AddressViewHolder(
                viewmodel,
                binding,
                prefHelper,
                firebaseAuth,
                messageDialog,
                updateAddressDialog,
                addressActivity)
        }

        override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
            holder.bindItem(
                AddressNetwork(
                    addresses[position].addressId,
                    addresses[position].userId,
                    addresses[position].addressName,
                    addresses[position].addressLine1,
                    addresses[position].addressLine2,
                    addresses[position].address_city,
                    addresses[position].city_name,
                    addresses[position].addressLat,
                    addresses[position].addressLng
                )
                , messageDialog)
        }

        override fun getItemCount(): Int {
            return addresses.size
        }
    }

}