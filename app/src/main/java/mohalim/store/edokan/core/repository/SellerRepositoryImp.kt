package mohalim.store.edokan.core.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.SellerInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.req.GetOrdersRequest
import mohalim.store.edokan.core.data_source.room.MarketplaceDao
import mohalim.store.edokan.core.model.marketplace.MarketPlace
import mohalim.store.edokan.core.model.marketplace.MarketplaceCacheMapper
import mohalim.store.edokan.core.model.marketplace.MarketplaceNetworkMapper
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import javax.inject.Inject

class SellerRepositoryImp
@Inject constructor(
    val retrofit: SellerInterfaceRetrofit,
    val networkMapper: MarketplaceNetworkMapper,
    val marketplaceCacheMapper: MarketplaceCacheMapper,
    val marketplaceDao: MarketplaceDao,
    val context: Context
) : SellerRepository {

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }

    override fun getMarketplaces(fToken: String): Flow<DataState<List<MarketPlace>>> {
        return flow{
            emit(DataState.Loading)
            try {
                val marketplaces = retrofit.getMarketPlaces("Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken())
                marketplaces.forEach { marketplace->
                    marketplaceDao.insert(marketplaceCacheMapper.mapToEntity(networkMapper.mapFromEntity(marketplace)))
                }
                emit(DataState.Success(networkMapper.mapFromEntityList(marketplaces)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getMarketplaceFromCache(marketplaceId: Int): Flow<DataState<MarketPlace>> {
        return flow {
                emit(DataState.Loading)
                try {
                    val marketplace = marketplaceDao.getProductById(marketplaceId = marketplaceId)
                    emit(DataState.Success(marketplaceCacheMapper.mapFromEntity(marketplace)))
                }catch (exception : Exception){
                    emit(DataState.Failure(exception))
                }
            }.flowOn(Dispatchers.IO)
    }

    override fun getOrders(limit: Int, offset: Int, marketplaceId : Int, fToken: String): Flow<DataState<List<Order>>> {
        return flow {
            emit(DataState.Loading)

            try {
                val orders = retrofit.getOrders(
                    GetOrdersRequest(preferenceHelper.getUserId().toString(), limit, offset),
                    marketplaceId,
                    "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken()
                )

                emit(DataState.Success(orders))

            }catch (e :Exception){
                Log.d("TAG", "getOrders: "+e.message)
                emit(DataState.Failure(e))
            }

        }.flowOn(Dispatchers.IO)
    }




}