package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.offer.OfferNetwork
import retrofit2.http.*


interface OfferInterfaceRetrofit {

    @HTTP(
            method = "GET",
            path = "/api/offers/{city_id}",
    )
    suspend fun getCurrentOffers(@Path("city_id")cityId : Int): List<OfferNetwork>

}