package mohalim.store.edokan.ui.address

import android.opengl.Visibility
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
import mohalim.store.edokan.ui.product.ProductViewModel

@AndroidEntryPoint
class AddressActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddressBinding;

    val viewModel : AddressViewModel by viewModels()

    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(this) }

    lateinit var addressRVAdpter : AddressRecyclerAdapter;

    private lateinit var addAddressDialog : AddAddressDialog;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_address)

        addAddressDialog = AddAddressDialog();

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

    private fun subscribeObservers() {
        viewModel.addresses.observe(this, Observer {
            when(it){
                is DataState.Loading ->{

                }
                is DataState.Success ->{
                    addressRVAdpter.addresses.clear()
                    addressRVAdpter.addresses.addAll(it.data)
                    addressRVAdpter.notifyDataSetChanged()
                }

                is DataState.Failure->{
                    Log.d("TAG", "subscribeObservers: "+ it.exception.message)
                }
            }
        })
    }

    private fun initAddressRV() {
        addressRVAdpter = AddressRecyclerAdapter(ArrayList(), preferenceHelper)
        val layoutManager = LinearLayoutManager(this@AddressActivity, LinearLayoutManager.VERTICAL, false)
        binding.addressRV.layoutManager = layoutManager
        binding.addressRV.adapter = addressRVAdpter

        val dividerItemDecoration : DividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.addressRV.addItemDecoration(dividerItemDecoration)
    }

    class AddressRecyclerAdapter (var addresses : MutableList<Address>, val prefHelper: IPreferenceHelper) : RecyclerView.Adapter<AddressRecyclerAdapter.AddressViewHolder>() {

        class AddressViewHolder (val binding : RowAddressBinding, val prefHelper: IPreferenceHelper) : RecyclerView.ViewHolder(binding.root) {
            fun bindItem(address: Address){
                binding.addressTitleTv.text = address.addressName
                binding.addressLine1Tv.text = address.addressLine1
                binding.addressLine2Tv.text = address.addressLine2 + " "+ address.city



                if (address.addressId == prefHelper.getDefaultAddressId()){
                    binding.defaultTv.visibility = View.VISIBLE
                    binding.setDefaultBtn.visibility = View.GONE
                }else{
                    binding.defaultTv.visibility = View.GONE
                    binding.setDefaultBtn.visibility = View.VISIBLE
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

            return AddressViewHolder(binding, prefHelper)
        }

        override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
            holder.bindItem(addresses[position])
        }

        override fun getItemCount(): Int {
            return addresses.size
        }
    }
}