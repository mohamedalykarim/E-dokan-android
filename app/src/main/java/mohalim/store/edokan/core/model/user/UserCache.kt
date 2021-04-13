package mohalim.store.edokan.core.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class UserCache(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id")
    var userId : String,

    @ColumnInfo(name = "phone_number")
    var phoneNumber : String,

    @ColumnInfo(name = "user_name")
    var userName : String,

    @ColumnInfo(name = "email")
    var email : String,

    @ColumnInfo(name = "default_address")
    var defaultAddress: String,

    @ColumnInfo(name = "default_address_id")
    var defaultAddressId: Int,

    @ColumnInfo(name = "profile_image")
    var profileImage : String
)
