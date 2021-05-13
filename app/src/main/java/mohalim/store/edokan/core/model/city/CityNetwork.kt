package mohalim.store.edokan.core.model.city

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CityNetwork (
    @SerializedName("city_id")
    @Expose
    var cityId : Int,

    @SerializedName("city_name")
    @Expose
    var cityName : String
)