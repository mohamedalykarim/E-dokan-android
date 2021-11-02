package mohalim.store.edokan.core.model.marketplace

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "marketplace")
data class MarketPlaceCache(
    @PrimaryKey
    @ColumnInfo(name = "marketplace_id")
    var marketplaceId : Int,

    @ColumnInfo(name = "marketplace_name")
    var marketplaceName : String,

    @ColumnInfo(name = "marketplace_lat")
    var lat: Double,

    @ColumnInfo(name = "marketplace_lng")
    var lng : Double,

    @ColumnInfo(name = "city_id")
    var cityId : Int,

    @ColumnInfo(name = "marketplace_owner_id")
    var marketplaceOwnerId : String
)
