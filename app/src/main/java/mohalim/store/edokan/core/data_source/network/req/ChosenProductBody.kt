package mohalim.store.edokan.core.data_source.network.req

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChosenProductBody(
    @SerializedName("page")
    @Expose
    val page : Int,

    @SerializedName("page_count")
    @Expose
    val pageCount : Int,

    @SerializedName("city_id")
    @Expose
    val cityId : Int
)
