package mohalim.store.edokan.core.repository

import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.cart.CartProduct
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.product_image.ProductImage
import mohalim.store.edokan.core.model.product_rating.ProductRating
import mohalim.store.edokan.core.utils.DataState

interface ProductRepository {
    fun getChosenProducts(page:Int, count:Int, cityId : Int) : Flow<DataState<List<Product>>>
    fun getSimilarProducts(cityId: Int, productName : String) : Flow<DataState<List<Product>>>
    fun getProductForCategory(categoryId : Int, randomId : Int, limit : Int , offset : Int) : Flow<DataState<List<Product>>>
    fun getProductById(productId: Int) : Flow<DataState<Product>>
    fun getProductImages(productId : Int): Flow<DataState<List<ProductImage>>>
    fun getProductRating(productId : Int): Flow<DataState<ProductRating>>
    fun addProductToCart(cartPrduct: CartProduct) : Flow<DataState<Boolean>>
    fun getCartProductFromInternal(productId: Int) : Flow<DataState<CartProduct>>
    suspend fun cartProdcutCountUpInternal(productId: Int)
    suspend fun cartProdcutCountDownInternal(productId: Int)
    suspend fun removeCartProduct(productId: Int)
    suspend fun removeProductsfromCart(): Flow<DataState<Boolean>>
    fun getAllCartProductFromInternal() : Flow<DataState<List<CartProduct>>>
    suspend fun getProductsFromInternal(productsIds: MutableList<String>) : Flow<DataState<List<Product>>>

}