package mohalim.store.edokan.core.data_source.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.store.edokan.core.model.product_image.ProductImageCache
import mohalim.store.edokan.core.model.user.UserCache

@Dao
interface ProductImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productImageCache : ProductImageCache) : Long

    @Query("SELECT * FROM product_image_cache")
    suspend fun getAll() : List<ProductImageCache>

    @Query("SELECT * FROM product_image_cache WHERE product_id = :productId")
    suspend fun getProductImagesFromCache(productId: Int): List<ProductImageCache>
}