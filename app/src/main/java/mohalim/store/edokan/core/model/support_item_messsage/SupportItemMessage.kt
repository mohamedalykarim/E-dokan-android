package mohalim.store.edokan.core.model.support_item_messsage

import java.math.BigInteger

data class SupportItemMessage(
    var id : Int,
    var supportItemId : Int,
    var senderId : String,
    var message : String,
    var date : BigInteger
)
