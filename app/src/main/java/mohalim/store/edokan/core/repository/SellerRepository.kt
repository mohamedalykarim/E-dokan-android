package mohalim.store.edokan.core.repository

import android.location.Location
import com.google.firebase.auth.FirebaseUser
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.marketplace.MarketPlace
import mohalim.store.edokan.core.model.marketplace.MarketPlaceNetWork
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.utils.DataState
import okhttp3.Response

interface SellerRepository {
    fun getMarketplaces(fToken: String) : Flow<DataState<List<MarketPlace>>>
    fun getOrders(limit: Int, offset: Int, marketplaceId : Int, fToken: String) : Flow<DataState<List<Order>>>
    fun getMarketplaceFromCache(marketplaceId: Int) : Flow<DataState<MarketPlace>>
    fun getOrderDetails(orderId: Int, marketplaceId: Int, fToken: String): Flow<DataState<Order>>
}