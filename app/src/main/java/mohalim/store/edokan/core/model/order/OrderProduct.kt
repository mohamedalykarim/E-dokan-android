package mohalim.store.edokan.core.model.order

data class OrderProduct(
    var order_products_id : Int,
    var order_id : Int,
    var product_id : Int,
    var product_count : Int,
    var product_price : Double,
    var discount : Double
)
