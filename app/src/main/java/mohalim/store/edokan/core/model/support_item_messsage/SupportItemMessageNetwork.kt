package mohalim.store.edokan.core.model.support_item_messsage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class SupportItemMessageNetwork(
    @SerializedName("support_item_message_id")
    @Expose
    var id : Int,

    @SerializedName("support_item_id")
    @Expose
    var supportItemId : Int,

    @SerializedName("support_item_message_sender_id")
    @Expose
    var senderId : String,

    @SerializedName("support_item_message")
    @Expose
    var message : String,

    @SerializedName("support_item_message_date")
    @Expose
    var date : BigInteger
)
