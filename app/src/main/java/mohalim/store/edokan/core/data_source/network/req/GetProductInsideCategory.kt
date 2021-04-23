package mohalim.store.edokan.core.data_source.network.req

import com.google.gson.annotations.SerializedName

data class GetProductInsideCategory(
        @SerializedName("category_id")
        var categoryId : Int,
        @SerializedName("random_id")
        var randomNumber : Int,
        @SerializedName("limit")
        var limit : Int,
        @SerializedName("offset")
        var offset : Int
)
