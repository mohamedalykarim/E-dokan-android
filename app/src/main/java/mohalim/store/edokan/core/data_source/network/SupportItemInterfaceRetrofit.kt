package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.data_source.network.req.AddSupportItemBody
import mohalim.store.edokan.core.model.support_item.SupportItemNetWork
import mohalim.store.edokan.core.model.support_item_messsage.SupportItemMessageNetwork
import retrofit2.http.*


interface SupportItemInterfaceRetrofit {

    @HTTP( method = "GET", path = "/api/support-item/{user_id}")
    suspend fun getSupportItems(
        @Path("user_id") userId : String,
        @Header("authorization") tokens: String
    ) : retrofit2.Response<List<SupportItemNetWork>>

    @HTTP( method = "POST", path = "/api/support-item/{user_id}", hasBody = true)
    suspend fun addSupportItem(
        @Path("user_id") userId : String,
        @Header("authorization") tokens: String,
        @Body addSupportItemBody: AddSupportItemBody
    ) : SupportItemNetWork

    @HTTP( method = "GET", path = "/api/support-item/messages/{item_id}")
    suspend fun getAllMessages(
        @Path("item_id") itemId : Int,
        @Header("authorization") tokens: String
    ) : List<SupportItemMessageNetwork>


}