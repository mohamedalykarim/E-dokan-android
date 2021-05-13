package mohalim.store.edokan.core.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.SupportItemInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.req.AddSupportItemBody
import mohalim.store.edokan.core.data_source.room.SupportItemDao
import mohalim.store.edokan.core.model.support_item.SupportItem
import mohalim.store.edokan.core.model.support_item.SupportItemCacheMapper
import mohalim.store.edokan.core.model.support_item.SupportItemNetWork
import mohalim.store.edokan.core.model.support_item.SupportItemNetworkMapper
import mohalim.store.edokan.core.model.support_item_messsage.SupportItemMessage
import mohalim.store.edokan.core.model.support_item_messsage.SupportItemMessageNetworkMapper
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import javax.inject.Inject


class SupportItemRepositoryImp
@Inject constructor(
    private val retrofit: SupportItemInterfaceRetrofit,
    private val networkMapper: SupportItemNetworkMapper,
    private val networkMessageMapper : SupportItemMessageNetworkMapper,
    private val dao : SupportItemDao,
    private val cacheMapper: SupportItemCacheMapper,
    context: Context
) : SupportItemRepository {

    private val tag = "SupportItemRep"

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

               val supportItems : List<SupportItemNetWork> = response.body()!!

               supportItems.forEach {
                   dao.insert(cacheMapper.mapToEntity(networkMapper.mapFromEntity(it)))
               }

               emit(DataState.Success(networkMapper.mapFromEntityList(supportItems)))

           }catch (e : Exception){
               emit(DataState.Failure(e))
           }

       }.flowOn(Dispatchers.IO)
    }

    override fun addSupportItem(
        userId: String,
        fToken: String,
        message: String
    ): Flow<DataState<SupportItem>> {
        return flow {
            emit(DataState.Loading)
            try {
                val supportItemNetWork = retrofit.addSupportItem(
                    userId,
                    "Bearer "+
                            fToken+
                            "///"+
                            preferenceHelper.getApiToken(),
                    AddSupportItemBody(message)
                )

                emit(DataState.Success(networkMapper.mapFromEntity(supportItemNetWork)))

            }catch (e : Exception){
                Log.d("TAG", "addSupportItem: "+ e.message)
                emit(DataState.Failure(e))
            }

        }.flowOn(Dispatchers.IO)
    }

    override fun getAllMessages(
        itemId: Int,
        fToken: String
    ): Flow<DataState<List<SupportItemMessage>>> {
        return flow{
            emit(DataState.Loading)

            try {
                val networkMessages = retrofit.getAllMessages(
                    itemId,
                    "Bearer "+
                            fToken+
                            "///"+
                            preferenceHelper.getApiToken()
                )

                emit(DataState.Success(networkMessageMapper.mapFromEntityList(networkMessages)))

            }catch (e : Exception){
                emit(DataState.Failure(e))
            }

        }.flowOn(Dispatchers.IO)
    }


}