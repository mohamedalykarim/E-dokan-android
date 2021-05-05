package mohalim.store.edokan.core.repository

import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.support_item.SupportItem
import mohalim.store.edokan.core.utils.DataState

interface SupportItemRepository {
    fun getSupportItems(userId: String, fToken: String) : Flow<DataState<List<SupportItem>>>
}