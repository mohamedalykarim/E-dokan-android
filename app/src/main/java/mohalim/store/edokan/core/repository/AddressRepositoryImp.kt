package mohalim.store.edokan.core.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.AddressInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.req.AddAddressBody
import mohalim.store.edokan.core.data_source.room.AddressDao
import mohalim.store.edokan.core.model.address.Address
import mohalim.store.edokan.core.model.address.AddressCacheMapper
import mohalim.store.edokan.core.model.address.AddressNetwork
import mohalim.store.edokan.core.model.address.AddressNetworkMapper
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import javax.inject.Inject

class AddressRepositoryImp
@Inject constructor(
    private val retrofit: AddressInterfaceRetrofit,
    private val networkMapper: AddressNetworkMapper,
    private val addressDao: AddressDao,
    private val cacheMapper: AddressCacheMapper,
    context: Context
) : AddressRepository {

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }

    override fun getAllAddresses(userId: String, fToken: String): Flow<DataState<List<Address>>> {
        return flow {
            emit(DataState.Loading)
            try {
                val networkAddress = retrofit.getAllAddresses(
                    userId,
                    "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken()
                )

                emit(DataState.Success(networkMapper.mapFromEntityList(networkAddress)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getAddress(addressId: Int, fToken: String): Flow<DataState<Address>> {
        return flow {
            emit(DataState.Loading)
            try {
                val networkAddress = retrofit.getAddress(
                    addressId,
                    "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken()
                )
                emit(DataState.Success(networkMapper.mapFromEntity(networkAddress)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun addAddress(address: AddressNetwork, isDeafault : Boolean, fToken: String): Flow<DataState<Boolean>> {
        Log.d("TAG", "addAddress: from repository")
        return flow {
            emit(DataState.Loading)

            val addressBody = AddAddressBody(
                preferenceHelper.getUserId().toString(),
                address.addressName,
                address.addressLine1,
                address.addressLine2,
                address.address_city,
                address.addressLat,
                address.addressLng,
                isDeafault
            )
            try {
                val insertedAddress = retrofit.addAddress(
                    preferenceHelper.getUserId().toString(),
                    addressBody,
                    "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken()
                )

                emit(DataState.Success(true))

            }catch (e : Exception){
                Log.d("TAG", "addAddress: "+e.message)
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun updateAddress(address: AddressNetwork, fToken: String): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading)

            val addressBody = AddAddressBody(
                preferenceHelper.getUserId().toString(),
                address.addressName,
                address.addressLine1,
                address.addressLine2,
                address.address_city,
                address.addressLat,
                address.addressLng,
                false
            )
            try {
                val insertedAddress = retrofit.updateAddress(
                    address.addressId,
                    addressBody,
                    "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken()
                )

                emit(DataState.Success(true))

            }catch (e : Exception){
                Log.d("TAG", "addAddress: "+e.message)
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)    }

    override fun deleteAddress(addressId: Int, fToken: String): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading)

            try {
                val response  = retrofit.deleteAddress(addressId, "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken())
                Log.d("TAG", "deleteAddress: updated")
                emit(DataState.Success(true))

            }catch (e : Exception){
                Log.d("TAG", "deleteAddress: "+ e.message)
                emit(DataState.Failure(e))
            }

        }.flowOn(Dispatchers.IO)
    }

    override fun setDefault(addressId: Int, fToken : String): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading)

            try {
                val response  = retrofit.setDefault(addressId, "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken())
                Log.d("TAG", "setDefault: updated")
                emit(DataState.Success(true))

            }catch (e : Exception){
                Log.d("TAG", "setDefault: "+ e.message)
                emit(DataState.Failure(e))
            }

        }.flowOn(Dispatchers.IO)
    }

}