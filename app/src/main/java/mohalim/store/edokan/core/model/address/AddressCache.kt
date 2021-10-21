package mohalim.store.edokan.core.model.address

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "addresses")
data class AddressCache(
    @PrimaryKey
    @ColumnInfo(name = "address_id")
    var addressId : Int,
    @ColumnInfo(name = "user_id")
    var userId : String,
    @ColumnInfo(name = "address_name")
    var addressName : String,
    @ColumnInfo(name = "address_line1")
    var addressLine1 : String,
    @ColumnInfo(name = "address_line2")
    var addressLine2: String,
    @ColumnInfo(name = "address_city_id")
    var city_id: Int,
    @ColumnInfo(name = "address_city")
    var city: String,
    @ColumnInfo(name = "address_lat")
    var addressLat : Double,
    @ColumnInfo(name = "address_lng")
    var addressLng : Double
)
