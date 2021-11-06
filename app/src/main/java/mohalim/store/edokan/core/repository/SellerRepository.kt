package mohalim.store.edokan.core.repository

import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.marketplace.MarketPlace
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.utils.DataState

interface SellerRepository {
    fun getMarketplaces(fToken: String) : Flow<DataState<List<MarketPlace>>>
    fun getOrders(limit: Int, offset: Int, marketplaceId : Int, fToken: String) : Flow<DataState<List<Order>>>
    fun getMarketplaceFromCache(marketplaceId: Int) : Flow<DataState<MarketPlace>>
    fun getOrderDetails(orderId: Int, marketplaceId: Int, fToken: String): Flow<DataState<Order>>
    fun getProducts(marketplaceId: Int, fToken: String?): Flow<DataState<List<Product>>>
}