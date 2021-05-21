package mohalim.store.edokan.core.data_source.network

import com.google.gson.JsonObject
import mohalim.store.edokan.core.data_source.network.req.GetDirectionsBody
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.offer.OfferNetwork
import retrofit2.Response
import retrofit2.http.*


interface OrderInterfaceRetrofit {

    @HTTP(
            method = "POST",
            path = "/api/orders/get_directions",
            hasBody = true
    )
    suspend fun getOrderPath(
        @Body directionsBody: GetDirectionsBody,
        @Header("authorization") tokens: String
    ): JsonObject

}