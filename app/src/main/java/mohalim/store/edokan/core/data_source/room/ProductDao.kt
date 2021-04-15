package mohalim.store.edokan.core.data_source.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.store.edokan.core.model.product.ProductCache

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productCache: ProductCache) : Long

    @Query("SELECT * FROM product")
    suspend fun getAll() : List<ProductCache>
}