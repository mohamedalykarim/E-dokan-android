package mohalim.store.edokan.ui.address

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.address.Address
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.repository.AddressRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(private val addressRepositoryImp: AddressRepositoryImp) : ViewModel(){

    private val _addresses : MutableLiveData<DataState<List<Address>>> = MutableLiveData()
    val addresses : LiveData<DataState<List<Address>>> get() = _addresses


    fun getAddressForUser(userId: String, token: String) {
        viewModelScope.launch {
            addressRepositoryImp.getAllAddresses(userId, token).collect {
                _addresses.value = it
            }
        }
    }


}