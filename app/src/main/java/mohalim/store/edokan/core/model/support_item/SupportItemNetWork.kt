package mohalim.store.edokan.core.model.support_item

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class SupportItemNetWork(
        @SerializedName("support_item_id")
        @Expose
        var supportItemId : Int,

        @SerializedName("user_id")
        @Expose
        var userId : String,

        @SerializedName("support_item_status")
        @Expose
        var supportItemStatus : Int,

        @SerializedName("support_item_date")
        @Expose
        var supportItemDate: BigInteger,

        @SerializedName("support_item_message")
        @Expose
        var message: String
)
