package mohalim.store.edokan.core.model.support_item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

@Entity(tableName = "support_item")
data class SupportItemCache(
        @PrimaryKey
        @ColumnInfo(name = "support_item_id")
        var supportItemId : Int,

        @ColumnInfo(name = "user_id")
        var userId : String,

        @ColumnInfo(name = "support_item_status")
        var supportItemStatus : Int,

        @ColumnInfo(name = "support_item_date")
        var supportItemDate: BigInteger,

        @ColumnInfo(name = "support_item_message")
        var message: String
)
