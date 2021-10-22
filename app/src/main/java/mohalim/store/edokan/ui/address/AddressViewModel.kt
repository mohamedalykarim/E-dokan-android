package mohalim.store.edokan.ui.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.address.Address
import mohalim.store.edokan.core.model.address.AddressNetwork
import mohalim.store.edokan.core.repository.AddressRepositoryImp
import mohalim.store.edokan.core.repository.UserRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val addressRepositoryImp: AddressRepositoryImp, private val userRepositoryImp: UserRepositoryImp) : ViewModel(){

    private val _addresses : MutableLiveData<DataState<List<Address>>> = MutableLiveData()
    val addresses : LiveData<DataState<List<Address>>> get() = _addresses

    private val _addAddressObserver : MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val addAddressObserver : LiveData<DataState<Boolean>> get() = _addAddressObserver

    private val _updateAddressObserver : MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val updateAddressObserver : LiveData<DataState<Boolean>> get() = _updateAddressObserver


    private val _setDefaultObserver : MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val setDefaultObserver : LiveData<DataState<Boolean>> get() = _setDefaultObserver

    private val _updateDataObserver : MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val updateDataObserver : LiveData<DataState<Boolean>> get() = _updateDataObserver

    private val _deleteAddressObserver : MutableLiveData<DataState<Boolean>> = MutableLiveData()
    val deleteAddressObserver : LiveData<DataState<Boolean>> get() = _deleteAddressObserver



    fun getAddressForUser(userId: String, token: String) {
        viewModelScope.launch {
            addressRepositoryImp.getAllAddresses(userId, token).collect {
                _addresses.value = it
            }
        }
    }

    fun addAddress(address: AddressNetwork, isDefault: Boolean, fToken : String) {
        viewModelScope.launch {
            addressRepositoryImp.addAddress(address, isDefault, fToken).collect {
                _addAddressObserver.value = it
            }
        }
    }

    fun updateUserData(fToken: String) {
        viewModelScope.launch {
            userRepositoryImp.updateUserData(fToken).collect {
                _updateDataObserver.value = it
            }
        }
    }

    fun setDefault(addressId: Int, fToken: String) {
        viewModelScope.launch {
            addressRepositoryImp.setDefault(addressId, fToken).collect {
                _setDefaultObserver.value = it
            }
        }
    }

    fun deleteAddress(addressId: Int, fToken: String) {
        viewModelScope.launch {
            addressRepositoryImp.deleteAddress(addressId , fToken).collect {
                _deleteAddressObserver.value = it
            }
        }
    }

    fun updateAddress(address: AddressNetwork?, fToken: String) {
        viewModelScope.launch {
            addressRepositoryImp.updateAddress(address!!, fToken).collect {
                _updateAddressObserver.value = it
            }
        }
    }




}