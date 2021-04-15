package mohalim.store.edokan.core.model.product

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigInteger

@Entity(tableName = "product")
data class ProductCache(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "product_id")
    var productId : Int,
    @ColumnInfo(name = "product_name")
    var productName : String,
    @ColumnInfo(name = "product_description")
    var productDescription : String,
    @ColumnInfo(name = "product_image")
    var productImage : String,
    @ColumnInfo(name = "product_price")
    var productPrice : Float,
    @ColumnInfo(name = "product_discount")
    var productDiscount : Float,
    @ColumnInfo(name = "product_weight")
    var productWeight : Double,
    @ColumnInfo(name = "product_length")
    var productLength : Double,
    @ColumnInfo(name = "product_width")
    var productWidth : Double,
    @ColumnInfo(name = "product_height")
    var productHeight : Double,
    @ColumnInfo(name = "marketplace_id")
    var marketPlaceId : Int,
    @ColumnInfo(name = "marketplace_name")
    var marketPlaceName : String,
    @ColumnInfo(name = "marketplace_lat")
    var marketPlaceLat : Double,
    @ColumnInfo(name = "marketplace_lng")
    var marketPlaceLng: Double,
    @ColumnInfo(name = "city_Id")
    var marketPlaceCityId : Int,
    @ColumnInfo(name = "city_name")
    var marketPlaceCityName : String,
    @ColumnInfo(name = "product_quantity")
    var productQuantity : Int,
    @ColumnInfo(name = "product_viewed")
    var productViewed : Int,
    @ColumnInfo(name = "product_status")
    var productStatus : Int,
    @ColumnInfo(name = "product_chosen")
    var productChosen : Int,
    @ColumnInfo(name = "date_added")
    var dateAdded : BigInteger,
    @ColumnInfo(name = "date_modified")
    var dateModified : BigInteger
)
