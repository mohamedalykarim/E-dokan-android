package mohalim.store.edokan.core.repository

import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.utils.DataState

interface ProductRepository {
    fun getChosenProducts(page:Int, count:Int) : Flow<DataState<List<Product>>>
}