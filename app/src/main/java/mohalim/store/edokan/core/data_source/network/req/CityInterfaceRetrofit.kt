package mohalim.store.edokan.core.data_source.network.req

import mohalim.store.edokan.core.model.city.CityNetwork
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.offer.OfferNetwork
import retrofit2.http.*


interface CityInterfaceRetrofit {

    @HTTP(
            method = "GET",
            path = "/api/city",
    )
    suspend fun getAllCities(): List<CityNetwork>

}