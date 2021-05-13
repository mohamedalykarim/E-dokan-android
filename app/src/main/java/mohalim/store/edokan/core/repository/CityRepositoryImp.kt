package mohalim.store.edokan.core.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.req.CityInterfaceRetrofit
import mohalim.store.edokan.core.model.city.City
import mohalim.store.edokan.core.model.city.CityNetworkMapper
import mohalim.store.edokan.core.utils.DataState
import java.lang.Exception
import javax.inject.Inject

class CityRepositoryImp
@Inject constructor(
    private val retrofit: CityInterfaceRetrofit,
    private val networkMapper : CityNetworkMapper,
    context: Context
) : CityRepository {

    override fun getAllCities(): Flow<DataState<List<City>>> {
        return flow {
            emit(DataState.Loading)
            try {
                val networkCities = retrofit.getAllCities()
                emit(DataState.Success(networkMapper.mapFromEntityList(networkCities)))

            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }


}