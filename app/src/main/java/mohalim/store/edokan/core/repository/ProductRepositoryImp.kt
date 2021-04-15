package mohalim.store.edokan.core.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.ProductInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.req.ChosenProductBody
import mohalim.store.edokan.core.data_source.room.ProductDao
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.product.ProductCacheMapper
import mohalim.store.edokan.core.model.product.ProductNetworkMapper
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import javax.inject.Inject

class ProductRepositoryImp
@Inject constructor(
    private val retrofit: ProductInterfaceRetrofit,
    private val productNetworkMapper: ProductNetworkMapper,
    private val productDao: ProductDao,
    private val productCacheMapper: ProductCacheMapper,
    context: Context
) : ProductRepository {

    val TAG : String = "ProductRepositoryImp"
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }


    override fun getChosenProducts(page : Int, count : Int): Flow<DataState<List<Product>>> {
        return flow {
            emit(DataState.Loading)
            try {
                val productsNetwork = retrofit.getChosenProducts(ChosenProductBody(page, count))
                productsNetwork.forEach {
                    productDao.insert(productCacheMapper.mapToEntity(productNetworkMapper.mapFromEntity(it)))
                }
                //todo get from cache
                emit(DataState.Success(productNetworkMapper.mapFromEntityList(productsNetwork)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)

    }


}