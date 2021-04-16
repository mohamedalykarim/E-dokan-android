package mohalim.store.edokan.core.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.utils.DataState

interface OfferRepository {
    fun getCurrentOffers() : Flow<DataState<List<Offer>>>
}