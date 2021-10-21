package mohalim.store.edokan.core.model.address

data class Address(
    var addressId : Int,
    var userId : String,
    var addressName : String,
    var addressLine1 : String,
    var addressLine2: String,
    var city_id: Int,
    var city: String,
    var addressLat : Double,
    var addressLng : Double
)
