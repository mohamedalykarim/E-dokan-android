package mohalim.store.edokan.core.data_source.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.category.CategoryCache

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoryCache: CategoryCache) : Long

    @Query("SELECT * FROM category")
    suspend fun getAll() : List<CategoryCache>

    @Query("SELECT * FROM category where category_parent = 0")
    suspend fun getNoParentCategories() : List<CategoryCache>

    @Query("SELECT * FROM category where category_id = :id")
    suspend fun getCategoryFromCacheById(id : String): CategoryCache

    @Query("SELECT * FROM category where category_parent = :id")
    suspend fun getCategoryFromCacheByParentId(id : String): List<CategoryCache>
}