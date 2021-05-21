package mohalim.store.edokan.core.data_source.network.req

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import mohalim.store.edokan.core.model.location.LocationItem

data class GetDirectionsBody(
    @SerializedName("origin")
    var origin : LocationItem,
    @SerializedName("destination")
    var destination : LocationItem,
    @SerializedName("locations")
    var locations : List<LocationItem>,
)
