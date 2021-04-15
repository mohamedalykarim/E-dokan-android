package mohalim.store.edokan.core.model.product

import java.math.BigInteger

data class Product(
    var productId : Int,
    var productName : String,
    var productDescription : String,
    var productImage : String,
    var productPrice : Float,
    var productDiscount : Float,
    var productWeight : Double,
    var productLength : Double,
    var productWidth : Double,
    var productHeight : Double,
    var marketPlaceId : Int,
    var marketPlaceName : String,
    var marketPlaceLat : Double,
    var marketPlaceLng: Double,
    var marketPlaceCityId : Int,
    var marketPlaceCityName : String,
    var productQuantity : Int,
    var productViewed : Int,
    var productStatus : Int,
    var productChosen : Int,
    var dateAdded : BigInteger,
    var dateModified : BigInteger
)
