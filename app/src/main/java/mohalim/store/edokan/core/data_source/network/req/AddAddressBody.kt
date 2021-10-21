package mohalim.store.edokan.core.data_source.network.req

data class AddAddressBody constructor(
    var address_name : String,
    var address_line1 : String,
    var address_line2 : String,
    var address_city_id : Int,
    var address_lat : Double,
    var address_lng : Double,
    var is_default : Boolean
)