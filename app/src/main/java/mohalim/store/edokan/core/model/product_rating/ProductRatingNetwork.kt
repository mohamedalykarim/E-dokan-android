package mohalim.store.edokan.core.model.product_rating

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductRatingNetwork(
        @SerializedName("product_id")
        @Expose
        var productId : Int,

        @SerializedName("product_rate")
        @Expose
        var productRate : Float,

        @SerializedName("review_count")
        @Expose
        var reviewCount : Int,

        @SerializedName("rating_5")
        @Expose
        var rating5Count : Int,

        @SerializedName("rating_4")
        @Expose
        var rating4Count : Int,

        @SerializedName("rating_3")
        @Expose
        var rating3Count : Int,

        @SerializedName("rating_2")
        @Expose
        var rating2Count : Int,

        @SerializedName("rating_1")
        @Expose
        var rating1Count : Int,
)
