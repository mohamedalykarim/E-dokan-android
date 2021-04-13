package mohalim.store.edokan.core.model.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserNetwork(

        @SerializedName("user_id")
        @Expose
        var userId :String,

        @SerializedName("phone_number")
        @Expose
        var phoneNumber : String,

        @SerializedName("user_name")
        @Expose
        var userName : String,

        @SerializedName("email")
        @Expose
        var email : String,

        @SerializedName("default_address")
        @Expose
        var defaultAddress: String,

        @SerializedName("default_address_id")
        @Expose
        var defaultAddressId: Int,

        @SerializedName("profile_image")
        @Expose
        var profileImage : String,

        @SerializedName("ftoken")
        @Expose
        var ftoken : String,

        @SerializedName("wtoken")
        @Expose
        var wtoken : String,
)
