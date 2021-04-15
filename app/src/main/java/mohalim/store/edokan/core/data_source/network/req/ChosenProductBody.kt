package mohalim.store.edokan.core.data_source.network.req

import com.google.gson.annotations.SerializedName

data class ChosenProductBody(
    @SerializedName("page")
    val page : Int,

    @SerializedName("page_count")
    val pageCount : Int
)
