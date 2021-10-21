package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.data_source.network.req.AddAddressBody
import mohalim.store.edokan.core.model.address.AddressNetwork
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.offer.OfferNetwork
import retrofit2.http.*


interface AddressInterfaceRetrofit {

    @HTTP(method = "GET", path = "/api/address/user/{user_id}")
    suspend fun getAllAddresses(@Path("user_id")userId : String, @Header("authorization") tokens: String
    ): List<AddressNetwork>

    @HTTP(method = "GET", path = "/api/address/{address_id}")
    suspend fun getAddress(
        @Path("address_id")addressId : Int,
        @Header("authorization") tokens: String
    ): AddressNetwork

    @HTTP(method = "POST", path = "/api/address/user/{user_id}", hasBody = true)
    suspend fun addAddress(
        @Path("user_id")userId: String,
        @Body address : AddAddressBody,
        @Header("authorization") tokens: String
    ): AddressNetwork

    @HTTP(method = "GET", path = "/api/address/default/{address_id}")
    suspend fun setDefault(
        @Path("address_id") addressId: Int,
        @Header("authorization") tokens: String
    ) : AddressNetwork

    @HTTP(method = "DELETE", path = "/api/address/{address_id}")
    suspend fun deleteAddress(
        @Path("address_id") addressId: Int,
        @Header("authorization") tokens: String
    ) : AddressNetwork


}