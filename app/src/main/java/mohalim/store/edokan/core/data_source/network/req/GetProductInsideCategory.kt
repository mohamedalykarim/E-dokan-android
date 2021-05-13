package mohalim.store.edokan.core.data_source.network.req

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetProductInsideCategory(
        @SerializedName("category_id")
        @Expose
        var categoryId : Int,
        @SerializedName("random_id")
        @Expose
        var randomNumber : Int,
        @SerializedName("limit")
        @Expose
        var limit : Int,
        @SerializedName("offset")
        @Expose
        var offset : Int,
        @SerializedName("city_id")
        @Expose
        var cityId : Int
)
