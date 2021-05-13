package mohalim.store.edokan.core.model.support_item_messsage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

@Entity(tableName = "support_item_message")
data class SupportItemMessageCache(
    @PrimaryKey
    @ColumnInfo(name = "support_item_message_id")
    var id : Int,

    @ColumnInfo(name = "support_item_id")
    var supportItemId : Int,

    @ColumnInfo(name = "support_item_message_sender_id")
    var senderId : String,

    @ColumnInfo(name = "support_item_message")
    var message : String,

    @ColumnInfo(name = "support_item_message_date")
    var date : BigInteger
)
