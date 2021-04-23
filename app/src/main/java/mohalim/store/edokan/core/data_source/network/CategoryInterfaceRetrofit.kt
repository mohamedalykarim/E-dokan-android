package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.data_source.network.req.GetCategoriesByParentBody
import mohalim.store.edokan.core.data_source.network.req.GetUserBody
import mohalim.store.edokan.core.model.category.CategoryNetWork
import mohalim.store.edokan.core.model.user.UserNetwork
import retrofit2.http.*


interface CategoryInterfaceRetrofit {

    @HTTP( method = "GET", path = "/api/categories/")
    suspend fun getNoParentCategories() : List<CategoryNetWork>


    @HTTP(method = "POST", path = "/api/categories/", hasBody = true)
    suspend fun getCategoriesByParentId(@Body body : GetCategoriesByParentBody) : List<CategoryNetWork>



}