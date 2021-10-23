package mohalim.store.edokan.core.data_source.network

import com.google.gson.JsonObject
import mohalim.store.edokan.core.data_source.network.req.AddOrderBody
import mohalim.store.edokan.core.data_source.network.req.GetDirectionsBody
import mohalim.store.edokan.core.data_source.network.req.GetOrdersRequest
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.offer.OfferNetwork
import mohalim.store.edokan.core.model.order.Order
import retrofit2.Response
import retrofit2.http.*


interface OrderInterfaceRetrofit {

    @HTTP(
            method = "POST",
            path = "/api/orders/get-cart-details",
            hasBody = true
    )
    suspend fun getOrderPath(
        @Body directionsBody: GetDirectionsBody,
        @Header("authorization") tokens: String
    ): JsonObject


    @HTTP(method = "POST", path="/api/orders/", hasBody = true)
    suspend fun addOrder(
        @Body orderBody : AddOrderBody,
        @Header("authorization") tokens: String
    ) : Order

    @HTTP(method = "GET", path = "/api/orders/{order_id}")
    suspend fun getOrderDetails(
        @Path("order_id") orderId: Int,
        @Header("authorization") tokens: String
    ): JsonObject

    @HTTP(method = "POST", path = "/api/orders/user/", hasBody = true)
    suspend fun getOrders(
        @Body getOrdersRequest: GetOrdersRequest,
        @Header("authorization") fToken: String
    ): List<Order>

}