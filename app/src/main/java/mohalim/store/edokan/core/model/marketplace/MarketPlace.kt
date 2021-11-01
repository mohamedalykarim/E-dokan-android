package mohalim.store.edokan.core.model.marketplace

data class MarketPlace(
    var marketplaceId : Int,
    var marketplaceName : String,
    var lat: Double,
    var lng : Double,
    var distanceToUser : Float,
    var cityId : Int,
    var marketplaceOwnerId : String
)
