package mohalim.store.edokan.core.model.address

import androidx.room.ColumnInfo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddressNetwork(
    @SerializedName("address_id")
    @Expose
    var addressId : Int,

    @SerializedName("user_id")
    @Expose
    var userId : String,

    @SerializedName("address_name")
    @Expose
    var addressName : String,

    @SerializedName("address_line1")
    @Expose
    var addressLine1 : String,

    @SerializedName("address_line2")
    @Expose
    var addressLine2: String,

    @SerializedName("address_city")
    @Expose
    var address_city: Int,

    @SerializedName("city_name")
    @Expose
    var city_name: String,

    @SerializedName("address_lat")
    @Expose
    var addressLat : Double,

    @SerializedName("address_lng")
    @Expose
    var addressLng : Double
)
