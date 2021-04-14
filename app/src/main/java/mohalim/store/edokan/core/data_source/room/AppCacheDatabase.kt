package mohalim.store.edokan.core.data_source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import mohalim.store.edokan.core.model.category.CategoryCache
import mohalim.store.edokan.core.model.user.UserCache

@Database(entities = [UserCache::class, CategoryCache::class], version = 1, exportSchema = false)
abstract class AppCacheDatabase : RoomDatabase(){

    abstract fun userDao() : UserDao
    abstract fun categoryDoa() : CategoryDao

    companion object{
        val DATABASE_NAME = "e-dokan_app_database"
    }
}