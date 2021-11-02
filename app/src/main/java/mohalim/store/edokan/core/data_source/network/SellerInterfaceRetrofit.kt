package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.data_source.network.req.GetOrdersRequest
import mohalim.store.edokan.core.model.marketplace.MarketPlaceNetWork
import mohalim.store.edokan.core.model.order.Order
import retrofit2.http.*


interface SellerInterfaceRetrofit {
    @HTTP(method = "GET", path = "/api/seller/marketplaces")
    suspend fun getMarketPlaces(@Header("authorization") tokens: String): List<MarketPlaceNetWork>

    @HTTP(method = "POST", path = "/api/seller/{marketplace_id}/orders", hasBody = true)
    suspend fun getOrders(
        @Body getOrdersRequest: GetOrdersRequest,
        @Path("marketplace_id") marketplaceId: Int,
        @Header("authorization") tokens: String): List<Order>

    @HTTP(method = "GET", path = "/api/seller/{marketplace_id}/order/{order_id}")
    suspend fun getOrderDetails(
        @Path("order_id") orderId: Int,
        @Path("marketplace_id") marketplaceId: Int,
        @Header("authorization") tokens: String
    ): Order
}