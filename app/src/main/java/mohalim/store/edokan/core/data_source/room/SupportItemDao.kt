package mohalim.store.edokan.core.data_source.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.store.edokan.core.model.support_item.SupportItemCache
import mohalim.store.edokan.core.model.user.UserCache

@Dao
interface SupportItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(supportItemCache: SupportItemCache) : Long

    @Query("SELECT * FROM support_item")
    suspend fun getAll() : List<SupportItemCache>
}