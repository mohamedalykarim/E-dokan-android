package mohalim.store.edokan.core.model.marketplace

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MarketPlaceNetWork(
    @SerializedName("marketplace_id")
    @Expose
    var marketplaceId : Int,
    @SerializedName("marketplace_name")
    @Expose
    var marketplaceName : String,
    @SerializedName("marketplace_lat")
    @Expose
    var lat: Double,
    @SerializedName("marketplace_lng")
    @Expose
    var lng : Double,
    @SerializedName("city_id")
    @Expose
    var cityId : Int,
    @SerializedName("marketplace_owner_id")
    @Expose
    var marketplaceOwnerId : String
)
