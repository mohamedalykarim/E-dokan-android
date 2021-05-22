package mohalim.store.edokan.core.data_source.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.store.edokan.core.model.address.AddressCache
import mohalim.store.edokan.core.model.offer.OfferCache
import mohalim.store.edokan.core.model.user.UserCache

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(addressCache: AddressCache) : Long

    @Query("SELECT * FROM addresses")
    suspend fun getAll() : List<AddressCache>
}