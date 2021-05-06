package mohalim.store.edokan.core.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.SupportItemInterfaceRetrofit
import mohalim.store.edokan.core.data_source.room.SupportItemDao
import mohalim.store.edokan.core.model.support_item.SupportItem
import mohalim.store.edokan.core.model.support_item.SupportItemCacheMapper
import mohalim.store.edokan.core.model.support_item.SupportItemNetWork
import mohalim.store.edokan.core.model.support_item.SupportItemNetworkMapper
import mohalim.store.edokan.core.model.user.UserNetwork
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import javax.inject.Inject


class SupportItemRepositoryImp
@Inject constructor(
    private val retrofit: SupportItemInterfaceRetrofit,
    private val networkMapper: SupportItemNetworkMapper,
    private val dao : SupportItemDao,
    private val cacheMapper: SupportItemCacheMapper,
    context: Context
) : SupportItemRepository {

    val TAG = "SupportItemRepositoryImp"

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }


    override fun getSupportItems(userId: String, fToken: String): Flow<DataState<List<SupportItem>>> {
       return flow {
           emit(DataState.Loading)

           try {
               val response : retrofit2.Response<List<SupportItemNetWork>> = retrofit.getSupportItems(
                       userId,
                       "Bearer "+
                               fToken+
                               "///"+
                               preferenceHelper.getApiToken()
               )

               Log.d(TAG, "getSupportItems: "+response.body())


               val supportItems : List<SupportItemNetWork> = response.body()!!;
               emit(DataState.Success(networkMapper.mapFromEntityList(supportItems)))

           }catch (e : Exception){
               Log.d(TAG, "getSupportItems: error "+ e.message)
               emit(DataState.Failure(e))
           }

       }.flowOn(Dispatchers.IO)
    }


}