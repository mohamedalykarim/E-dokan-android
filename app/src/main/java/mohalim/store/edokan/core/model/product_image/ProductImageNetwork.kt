package mohalim.store.edokan.core.model.product_image

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductImageNetwork(
        @SerializedName("product_image_id")
        @Expose
        var id : Int,
        @SerializedName("product_id")
        @Expose
        var productId : Int,
        @SerializedName("product_image")
        @Expose
        var productImage : String
)
