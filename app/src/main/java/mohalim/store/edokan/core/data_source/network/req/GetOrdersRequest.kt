package mohalim.store.edokan.core.data_source.network.req

data class GetOrdersRequest(
    var user_id : String,
    var limit : Int,
    var offset : Int
)
