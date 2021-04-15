package mohalim.store.edokan.core.data_source.network.req

import com.google.gson.annotations.SerializedName

data class GetUserBody
constructor (
        @SerializedName("user_id")
        val userId : String?,

        @SerializedName("phone_number")
        val phoneNumber : String?,

        @SerializedName("password")
        val password : String?

) {
}