package mohalim.store.edokan.core.model.support_item

import java.math.BigInteger

data class SupportItem(
        var supportItemId : Int,
        var userId : String,
        var supportItemStatus : Int,
        var supportItemDate: BigInteger,
        var message : String
)
