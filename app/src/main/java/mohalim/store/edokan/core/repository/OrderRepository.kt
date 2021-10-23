package mohalim.store.edokan.core.repository

import android.location.Location
import com.google.firebase.auth.FirebaseUser
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.utils.DataState
import okhttp3.Response

interface OrderRepository {
    fun getOrderPath(
        origin : Location,
        destination : Location,
        locations : MutableList<Location>,
        productIds: List<Int>,
        counts : List<Int>,
        fToken: String
    ) : Flow<DataState<JsonObject>>

    fun addOrder(order : Order, fToken: String) : Flow<DataState<Order>>
    fun getOrderDetails(orderId: Int, fToken: String) : Flow<DataState<Response>>
    fun getOrders(limit: Int, offset: Int, fToken: String): Flow<DataState<List<Order>>>
}