package mohalim.store.edokan.core.data_source.network

import mohalim.store.edokan.core.data_source.network.req.ChosenProductBody
import mohalim.store.edokan.core.data_source.network.req.GetProductInsideCategory
import mohalim.store.edokan.core.model.product.ProductNetwork
import mohalim.store.edokan.core.model.product_image.ProductImageNetwork
import retrofit2.http.*


interface ProductInterfaceRetrofit {

    @HTTP(method = "POST", path = "/api/products/chosen-products", hasBody = true)
    suspend fun getChosenProducts(@Body chosenProductBody: ChosenProductBody): List<ProductNetwork>

    @HTTP(method = "POST", path = "/api/products/category", hasBody = true)
    suspend fun getProductForCategory(@Body getProductInsideCategory: GetProductInsideCategory): List<ProductNetwork>

    @HTTP(method = "GET", path = "/api/products/images/{productId}")
    suspend fun getProductImages(@Path("productId") productId : Int): List<ProductImageNetwork>



}