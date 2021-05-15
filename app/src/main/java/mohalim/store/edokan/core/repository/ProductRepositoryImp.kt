package mohalim.store.edokan.core.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import mohalim.store.edokan.core.data_source.network.ProductInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.req.ChosenProductBody
import mohalim.store.edokan.core.data_source.network.req.GetProductInsideCategory
import mohalim.store.edokan.core.data_source.room.CartProductDao
import mohalim.store.edokan.core.data_source.room.ProductDao
import mohalim.store.edokan.core.data_source.room.ProductImageDao
import mohalim.store.edokan.core.model.cart.CartProduct
import mohalim.store.edokan.core.model.cart.CartProductCacheMapper
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.product.ProductCacheMapper
import mohalim.store.edokan.core.model.product.ProductNetworkMapper
import mohalim.store.edokan.core.model.product_image.ProductImage
import mohalim.store.edokan.core.model.product_image.ProductImageCacheMapper
import mohalim.store.edokan.core.model.product_image.ProductImageNetworkMapper
import mohalim.store.edokan.core.model.product_rating.ProductRating
import mohalim.store.edokan.core.model.product_rating.ProductRatingNetworkMapper
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import java.security.spec.ECField
import javax.inject.Inject

class ProductRepositoryImp
@Inject constructor(
    private val retrofit: ProductInterfaceRetrofit,
    private val productNetworkMapper: ProductNetworkMapper,
    private val productDao: ProductDao,
    private val productCacheMapper: ProductCacheMapper,
    private val productImageDao: ProductImageDao,
    private val productImageNetworkMapper: ProductImageNetworkMapper,
    private val productImageCacheMapper: ProductImageCacheMapper,
    private val productRatingNetworkMapper: ProductRatingNetworkMapper,
    private val cartProductDao: CartProductDao,
    private val cartProductCacheMapper : CartProductCacheMapper,
    context: Context
) : ProductRepository {

    val TAG : String = "ProductRepositoryImp"
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }


    override fun getChosenProducts(page : Int, count : Int, cityId: Int): Flow<DataState<List<Product>>> {
        return flow {
            emit(DataState.Loading)
            try {
                val productsNetwork = retrofit.getChosenProducts(ChosenProductBody(page, count, cityId))
                productsNetwork.forEach {
                    productDao.insert(productCacheMapper.mapToEntity(productNetworkMapper.mapFromEntity(it)))
                }
                emit(DataState.Success(productNetworkMapper.mapFromEntityList(productsNetwork)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)

    }

    override fun getSimilarProducts(cityId: Int, productName: String): Flow<DataState<List<Product>>> {
        return flow {
            emit(DataState.Loading)
            try {
                val productsNetwork = retrofit.getSimilarProducts(cityId, productName)
                productsNetwork.forEach {
                    productDao.insert(productCacheMapper.mapToEntity(productNetworkMapper.mapFromEntity(it)))
                }
                emit(DataState.Success(productNetworkMapper.mapFromEntityList(productsNetwork)))
            }catch (e : Exception){
                Log.d(TAG, "getSimilarProducts: "+ e.message)
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getProductForCategory(categoryId: Int, randomId: Int, limit: Int, offset: Int): Flow<DataState<List<Product>>> {
        return flow {
            emit(DataState.Loading)
            try {
                val productsNetwork = retrofit.getProductForCategory(GetProductInsideCategory(categoryId,randomId, limit, offset, preferenceHelper.getCityId()!!))
                productsNetwork.forEach {
                    productDao.insert(productCacheMapper.mapToEntity(productNetworkMapper.mapFromEntity(it)))
                }
                emit(DataState.Success(productNetworkMapper.mapFromEntityList(productsNetwork)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getProductById(productId: Int): Flow<DataState<Product>> {
        return flow {
            emit(DataState.Loading)
            try {
                val productsCache = productDao.getProductById(productId)
                emit(DataState.Success(productCacheMapper.mapFromEntity(productsCache)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getProductImages(productId: Int): Flow<DataState<List<ProductImage>>> {
        return flow {
            emit(DataState.Loading)
            try {
                val productImagesNetwork = retrofit.getProductImages(productId)
                productImagesNetwork.forEach {
                    productImageDao.insert(productImageCacheMapper.mapToEntity(productImageNetworkMapper.mapFromEntity(it)))
                }
                emit(DataState.Success(productImageNetworkMapper.mapFromEntityList(productImagesNetwork)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getProductRating(productId: Int): Flow<DataState<ProductRating>> {
        return flow {
            emit(DataState.Loading)
            try {
                val productRating = retrofit.getProductRating(productId)
                emit(DataState.Success(productRatingNetworkMapper.mapFromEntity(productRating)))
            }catch (e : Exception){
                Log.d(TAG, "getProductRating: "+e.message)
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun addProductToCart(cartPrduct: CartProduct): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading)
            try {
                val inserted = cartProductDao.insert(cartProductCacheMapper.mapToEntity(cartPrduct))
                emit(DataState.Success(true))

            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getCartProductFromInternal(productId: Int): Flow<DataState<CartProduct>> {
        return flow {
            emit(DataState.Loading)
            try {
                val cartProduct = cartProductDao.getProductById(productId)
                emit(DataState.Success(cartProductCacheMapper.mapFromEntity(cartProduct)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun cartProdcutCountUpInternal(productId: Int) {
        withContext(Dispatchers.IO){
            val updated = cartProductDao.countUp(productId)
        }
    }

    override suspend fun cartProdcutCountDownInternal(productId: Int) {
        withContext(Dispatchers.IO){
            val updated = cartProductDao.countDown(productId)
        }
    }

    override suspend fun removeCartProduct(productId: Int){
        withContext(Dispatchers.IO){
            val updated = cartProductDao.remove(productId)
        }
    }

}