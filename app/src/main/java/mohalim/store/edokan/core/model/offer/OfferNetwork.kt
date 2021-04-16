package mohalim.store.edokan.core.model.offer

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class OfferNetwork(
        @SerializedName("offer_id")
        @Expose
        var offerId : Int,

        @SerializedName("offer_type")
        @Expose
        var offerType : Int,

        @SerializedName("product_id")
        @Expose
        var productId : Int,

        @SerializedName("offer_image")
        @Expose
        var offerImage : String,

        @SerializedName("offer_name")
        @Expose
        var offerName : String,

        @SerializedName("offer_description")
        @Expose
        var offerDescription : String,

        @SerializedName("offer_status")
        @Expose
        var offerStatus : Int,

        @SerializedName("offer_end_date")
        @Expose
        var offerEndDate : BigInteger,

        @SerializedName("added_date")
        @Expose
        var addedDate: BigInteger,

        @SerializedName("modified_date")
        @Expose
        var modifiedDate : BigInteger

)
