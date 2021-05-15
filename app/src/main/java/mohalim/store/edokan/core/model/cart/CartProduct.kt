package mohalim.store.edokan.core.model.cart

data class CartProduct(
    var productId : Int,
    var productName : String,
    var productDescription : String,
    var productImage : String,
    var productPrice : Float,
    var productDiscount : Float,
    var marketPlaceId : Int,
    var marketPlaceName : String,
    var productCount: Int,
)
