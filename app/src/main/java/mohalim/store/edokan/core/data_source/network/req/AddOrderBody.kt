package mohalim.store.edokan.core.data_source.network.req

import mohalim.store.edokan.core.model.order.OrderMarketplace
import mohalim.store.edokan.core.model.order.OrderProduct

data class AddOrderBody(
    var user_id : String,
    var address_name : String,
    var address_line1 : String,
    var address_line2 : String,
    var city_id : Int,
    var address_lat : Double,
    var address_lng : Double,
    var order_products : List<OrderProduct>,
    var order_marketplaces : List<OrderMarketplace>
)