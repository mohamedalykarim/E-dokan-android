package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.data_source.network.req.GetUserBody
import mohalim.store.edokan.core.model.user.UserNetwork
import retrofit2.http.*


interface UserInterfaceRetrofit {

    @HTTP(
            method = "POST",
            path = "/api/users/login-after-phone",
            hasBody = true
    )
    suspend fun loginUserAfterPhone(
            @Body body : GetUserBody,
            @Header("Authorization") token: String
    ) : UserNetwork

}