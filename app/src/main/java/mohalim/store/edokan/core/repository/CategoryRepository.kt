package mohalim.store.edokan.core.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.utils.DataState

interface CategoryRepository {
    fun getNoParentCategories() : Flow<DataState<List<Category>>>
    fun getCategoryFromCacheById(id : Int) : Flow<DataState<Category>>
}