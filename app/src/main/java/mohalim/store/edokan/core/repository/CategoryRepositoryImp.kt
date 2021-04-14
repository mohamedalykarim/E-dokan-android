package mohalim.store.edokan.core.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.CategoryInterfaceRetrofit
import mohalim.store.edokan.core.data_source.room.CategoryDao
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.category.CategoryCacheMapper
import mohalim.store.edokan.core.model.category.CategoryNetworkMapper
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import javax.inject.Inject

class CategoryRepositoryImp
@Inject constructor(
    private val retrofit: CategoryInterfaceRetrofit,
    private val categoryNetworkMapper: CategoryNetworkMapper,
    private val categoryDao: CategoryDao,
    private val categoryCacheMapper: CategoryCacheMapper,
    context: Context
) : CategoryRepository {

    val TAG : String = "CategoryRepositoryImp"
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }


    override fun getNoParentCategories(): Flow<DataState<List<Category>>> {

        return flow {
            try {
                val categoriesNetwork = retrofit.getNoParentCategories()
                categoriesNetwork.forEach{
                    Log.d(TAG, "getNoParentCategories: "+ it)
                    categoryDao.insert(categoryCacheMapper.mapToEntity(categoryNetworkMapper.mapFromEntity(it)))
                }
                val categories : List<Category> = categoryCacheMapper.mapFromEntityList(categoryDao.getAll());
                emit(DataState.Success(categories))
            }catch (e : Exception){
                emit(DataState.Failure(e))
            }

        }.flowOn(Dispatchers.IO)


    }


}