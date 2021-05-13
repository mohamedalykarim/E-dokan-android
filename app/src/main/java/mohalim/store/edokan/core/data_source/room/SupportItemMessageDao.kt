package mohalim.store.edokan.core.data_source.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.store.edokan.core.model.support_item.SupportItemCache
import mohalim.store.edokan.core.model.support_item_messsage.SupportItemMessageCache
import mohalim.store.edokan.core.model.user.UserCache

@Dao
interface SupportItemMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(supportItemMessageCache: SupportItemMessageCache) : Long

    @Query("SELECT * FROM support_item_message")
    suspend fun getAll() : List<SupportItemMessageCache>
}