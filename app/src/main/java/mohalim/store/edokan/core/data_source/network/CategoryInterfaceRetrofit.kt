package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.data_source.network.req.GetUserBody
import mohalim.store.edokan.core.model.category.CategoryNetWork
import mohalim.store.edokan.core.model.user.UserNetwork
import retrofit2.http.*


interface CategoryInterfaceRetrofit {

    @HTTP(
            method = "GET",
            path = "/api/category/",
    )
    suspend fun getNoParentCategories(
    ) : List<CategoryNetWork>

}