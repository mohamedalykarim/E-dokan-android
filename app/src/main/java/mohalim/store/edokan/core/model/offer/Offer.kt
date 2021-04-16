package mohalim.store.edokan.core.model.offer

import java.math.BigInteger

data class Offer(
        var offerId : Int,
        var offerType : Int,
        var productId : Int,
        var offerImage : String,
        var offerName : String,
        var offerDescription : String,
        var offerStatus : Int,
        var offerEndDate : BigInteger,
        var addedDate: BigInteger,
        var modifiedDate : BigInteger

)
