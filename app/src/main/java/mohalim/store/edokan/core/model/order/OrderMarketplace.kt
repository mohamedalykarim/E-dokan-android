package mohalim.store.edokan.core.model.order

data class OrderMarketplace (
    var order_marketplaces_id : Int,
    var order_id : Int,
    var marketplace_id : Int,
    var marketplace_name : String,
    var marketplace_lat : Double,
    var marketplace_lng : Double
)