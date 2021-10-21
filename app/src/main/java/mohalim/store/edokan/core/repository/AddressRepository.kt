package mohalim.store.edokan.core.repository

import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.address.Address
import mohalim.store.edokan.core.model.address.AddressNetwork
import mohalim.store.edokan.core.utils.DataState

interface AddressRepository {
    fun getAllAddresses(userId: String, fToken: String) : Flow<DataState<List<Address>>>
    fun getAddress(addressId: Int, fToken: String) : Flow<DataState<Address>>
    fun addAddress(address: AddressNetwork, isDefault : Boolean,  fToken: String) : Flow<DataState<Boolean>>
    fun updateAddress(address : Address, fToken: String) : Flow<DataState<Boolean>>
    fun deleteAddress(addressId: String, fToken: String) : Flow<DataState<Boolean>>
}