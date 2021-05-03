package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.data_source.network.req.GetUserBody
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.model.user.UserNetwork
import okhttp3.Response
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
    ) : retrofit2.Response<UserNetwork>

}