package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.model.support_item.SupportItemNetWork
import retrofit2.http.*


interface SupportItemInterfaceRetrofit {

    @HTTP( method = "GET", path = "/api/support-item/{user_id}")
    suspend fun getSupportItems(
            @Path("user_id") userId : String,
            @Header("authorization") tokens: String
    ) : retrofit2.Response<List<SupportItemNetWork>>

}