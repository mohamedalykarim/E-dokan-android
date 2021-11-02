package mohalim.store.edokan.core.data_source.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.store.edokan.core.model.marketplace.MarketPlaceCache
import mohalim.store.edokan.core.model.product.ProductCache

@Dao
interface MarketplaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(marketPlaceCache: MarketPlaceCache) : Long

    @Query("SELECT * FROM marketplace")
    suspend fun getAll() : List<MarketPlaceCache>

    @Query("SELECT * FROM marketplace WHERE marketplace_id = :marketplaceId")
    suspend fun getProductById(marketplaceId: Int) : MarketPlaceCache

}