package mohalim.store.edokan.core.data_source.room.converter

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.store.edokan.core.model.offer.OfferCache
import mohalim.store.edokan.core.model.user.UserCache

@Dao
interface OfferDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(offerCache: OfferCache) : Long

    @Query("SELECT * FROM offer")
    suspend fun getAll() : List<OfferCache>
}