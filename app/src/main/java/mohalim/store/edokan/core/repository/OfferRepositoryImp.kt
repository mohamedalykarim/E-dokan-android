package mohalim.store.edokan.core.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.OfferInterfaceRetrofit
import mohalim.store.edokan.core.data_source.room.OfferDao
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.offer.OfferCacheMapper
import mohalim.store.edokan.core.model.offer.OfferNetworkMapper
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import javax.inject.Inject

class OfferRepositoryImp
@Inject constructor(
    private val retrofit: OfferInterfaceRetrofit,
    private val offerNetworkMapper: OfferNetworkMapper,
    private val offerDao: OfferDao,
    private val offerCacheMapper: OfferCacheMapper,
    context: Context
) : OfferRepository {

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }

    override fun getCurrentOffers(): Flow<DataState<List<Offer>>> {
        return flow {
            emit(DataState.Loading)
            try {
                val offers = retrofit.getCurrentOffers()
                emit(DataState.Success(offerNetworkMapper.mapFromEntityList(offers)))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)

    }


}