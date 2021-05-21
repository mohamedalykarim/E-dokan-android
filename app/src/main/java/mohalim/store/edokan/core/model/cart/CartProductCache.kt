package mohalim.store.edokan.core.model.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_product")
data class CartProductCache(
    @PrimaryKey
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
    @ColumnInfo(name = "marketplace_id")
    var marketPlaceId : Int,
    @ColumnInfo(name = "marketplace_name")
    var marketPlaceName : String,
    @ColumnInfo(name = "marketplace_lat")
    var marketPlaceLat : Double,
    @ColumnInfo(name = "marketplace_lan")
    var marketPlaceLng : Double,
    @ColumnInfo(name = "product_count")
    var productCount: Int,
)
