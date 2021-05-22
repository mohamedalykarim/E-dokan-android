package mohalim.store.edokan.core.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.AddressInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.OfferInterfaceRetrofit
import mohalim.store.edokan.core.data_source.room.AddressDao
import mohalim.store.edokan.core.data_source.room.OfferDao
import mohalim.store.edokan.core.model.address.Address
import mohalim.store.edokan.core.model.address.AddressCacheMapper
import mohalim.store.edokan.core.model.address.AddressNetworkMapper
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.offer.OfferCacheMapper
import mohalim.store.edokan.core.model.offer.OfferNetworkMapper
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

    override fun addAddress(address: Address, fToken: String): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading)
            try {
                val insertedAddress = retrofit.addAddress(
                    preferenceHelper.getUserId().toString(),
                    networkMapper.mapToEntity(address),
                    "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken()
                )

                emit(DataState.Success(true))

            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun updateAddress(address: Address, fToken: String): Flow<DataState<Boolean>> {
        TODO("Not yet implemented")
    }

    override fun deleteAddress(addressId: String, fToken: String): Flow<DataState<Boolean>> {
        TODO("Not yet implemented")
    }

}