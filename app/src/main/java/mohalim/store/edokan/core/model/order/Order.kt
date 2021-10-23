package mohalim.store.edokan.core.model.order

data class Order(
    var user_id : String,
    var address_name : String,
    var address_line1 : String,
    var address_line2 : String,
    var city_id : Int,
    var city_name : String,
    var address_lat : Double,
    var address_lng : Double,
    var delivery_id : Int,
    var current_delivery_location_lat : Double,
    var current_delivery_location_lng : Double,
    var status : Int,
    var distance : Double,
    var delivery_method : Int,
    var value : Double,
    var delivery_value : Double,
    var discount : Double,
    var order_products : List<OrderProduct>,
    var order_marketplaces : List<OrderMarketplace>
)