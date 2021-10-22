package mohalim.store.edokan.core.model.address

data class Address(
    var addressId : Int,
    var userId : String,
    var addressName : String,
    var addressLine1 : String,
    var addressLine2: String,
    var address_city: Int,
    var city_name: String,
    var addressLat : Double,
    var addressLng : Double
)
