package mohalim.store.edokan.core.repository

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.OrderInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.req.GetDirectionsBody
import mohalim.store.edokan.core.model.location.LocationItem
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import javax.inject.Inject

class OrderRepositoryImp
@Inject constructor(
    private val retrofit: OrderInterfaceRetrofit,
    context: Context
) : OrderRepository {

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }

    override fun getOrderPath(
        origin : Location,
        destination : Location,
        locations : MutableList<Location>,
        productIds : List<Int>,
        productCounts : List<Int>,
        fToken: String
    ): Flow<DataState<JsonObject>> {
        return flow {
            emit(DataState.Loading)
            try {

                val waypoints = ArrayList<LocationItem>()
                locations.remove(origin)

                locations.forEach {
                    waypoints.add(LocationItem(it.latitude, it.longitude))
                }

                val body = GetDirectionsBody(
                    LocationItem(origin.latitude,origin.longitude),
                    LocationItem(destination.latitude,destination.longitude),
                    waypoints,
                    productIds,
                    productCounts
                )

                val response = retrofit.getOrderPath(
                    body,
                    "Bearer "+
                            fToken+
                            "///"+
                            preferenceHelper.getApiToken()
                )

                emit(DataState.Success(response))

            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }


}