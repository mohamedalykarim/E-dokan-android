package mohalim.store.edokan.core.repository

import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.product_image.ProductImage
import mohalim.store.edokan.core.utils.DataState

interface ProductRepository {
    fun getChosenProducts(page:Int, count:Int) : Flow<DataState<List<Product>>>
    fun getProductForCategory(categoryId : Int, randomId : Int, limit : Int , offset : Int) : Flow<DataState<List<Product>>>
    fun getProductImages(productId : Int): Flow<DataState<List<ProductImage>>>
}