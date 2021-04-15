package mohalim.store.edokan.core.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class ProductNetwork(
    @SerializedName("product_id")
    @Expose()
    var productId : Int,

    @SerializedName("product_name")
    @Expose()
    var productName : String,

    @SerializedName("product_description")
    @Expose()
    var productDescription : String,

    @SerializedName("product_image")
    @Expose()
    var productImage : String,

    @SerializedName("product_price")
    @Expose()
    var productPrice : Float,

    @SerializedName("product_discount")
    @Expose()
    var productDiscount : Float,

    @SerializedName("product_weight")
    @Expose()
    var productWeight : Double,

    @SerializedName("product_length")
    @Expose()
    var productLength : Double,

    @SerializedName("product_width")
    @Expose()
    var productWidth : Double,

    @SerializedName("product_height")
    @Expose()
    var productHeight : Double,

    @SerializedName("marketplace_id")
    @Expose()
    var marketPlaceId : Int,

    @SerializedName("marketplace_name")
    @Expose()
    var marketPlaceName : String,

    @SerializedName("marketplace_lat")
    @Expose()
    var marketPlaceLat : Double,

    @SerializedName("marketplace_lng")
    @Expose()
    var marketPlaceLng: Double,

    @SerializedName("city_Id")
    @Expose()
    var marketPlaceCityId : Int,

    @SerializedName("city_name")
    @Expose()
    var marketPlaceCityName : String,

    @SerializedName("product_quantity")
    @Expose()
    var productQuantity : Int,

    @SerializedName("product_viewed")
    @Expose()
    var productViewed : Int,

    @SerializedName("product_status")
    @Expose()
    var productStatus : Int,

    @SerializedName("product_chosen")
    @Expose()
    var productChosen : Int,

    @SerializedName("date_added")
    @Expose()
    var dateAdded : BigInteger,

    @SerializedName("date_modified")
    @Expose()
    var dateModified : BigInteger
)
