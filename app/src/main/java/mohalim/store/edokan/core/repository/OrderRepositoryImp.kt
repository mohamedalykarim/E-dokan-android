package mohalim.store.edokan.core.repository

import android.content.Context
import android.location.Location
import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.OrderInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.req.AddOrderBody
import mohalim.store.edokan.core.data_source.network.req.GetDirectionsBody
import mohalim.store.edokan.core.data_source.network.req.GetOrdersRequest
import mohalim.store.edokan.core.model.location.LocationItem
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import okhttp3.Response
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

    override fun addOrder(order: Order, fToken: String): Flow<DataState<Order>> {
        return flow {
            emit(DataState.Loading)

            try{

                val orderBody = AddOrderBody(
                    order.user_id,
                    order.address_name,
                    order.address_line1,
                    order.address_line2,
                    order.city_id,
                    order.address_lat,
                    order.address_lng,
                    order.order_products,
                    order.order_marketplaces
                )

                val order = retrofit.addOrder(orderBody, "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken())

                emit(DataState.Success(order))

            }catch (e : Exception){
                Log.d("TAG", "addOrder: "+e.message)
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getOrderDetails(orderId: Int, fToken: String): Flow<DataState<Response>> {
        return flow {
            emit(DataState.Loading)

            try {
                val response = retrofit.getOrderDetails(orderId, "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken())
                Log.d("TAG", "getOrderDetails: "+ response)

            }catch (e :Exception){
                Log.d("TAG", "getOrderDetails: "+e.message)
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getOrders(limit: Int, offset: Int, fToken: String): Flow<DataState<List<Order>>> {
        return flow {
            emit(DataState.Loading)

            try {
                val orders = retrofit.getOrders(
                    GetOrdersRequest(preferenceHelper.getUserId().toString(), limit, offset),
                    "Bearer "+ fToken+ "///"+ preferenceHelper.getApiToken()
                )

                emit(DataState.Success(orders))

            }catch (e :Exception){
                Log.d("TAG", "getOrders: "+e.message)
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }


}