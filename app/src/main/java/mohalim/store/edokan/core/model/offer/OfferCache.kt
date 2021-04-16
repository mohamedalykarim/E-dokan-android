package mohalim.store.edokan.core.model.offer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigInteger

@Entity(tableName = "offer")
data class OfferCache (
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "offer_id")
        var offerId : Int,

        @ColumnInfo(name = "offer_type")
        var offerType : Int,

        @ColumnInfo(name = "product_id")
        var productId : Int,

        @ColumnInfo(name = "offer_image")
        var offerImage : String,

        @ColumnInfo(name = "offer_name")
        var offerName : String,

        @ColumnInfo(name = "offer_description")
        var offerDescription : String,

        @ColumnInfo(name = "offer_status")
        var offerStatus : Int,

        @ColumnInfo(name = "offer_end_date")
        var offerEndDate : BigInteger,

        @ColumnInfo(name = "added_date")
        var addedDate: BigInteger,

        @ColumnInfo(name = "modified_date")
        var modifiedDate : BigInteger

)