package mohalim.store.edokan.core.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohalim.store.edokan.core.data_source.room.AppCacheDatabase
import mohalim.store.edokan.core.data_source.room.CategoryDao
import mohalim.store.edokan.core.data_source.room.ProductDao
import mohalim.store.edokan.core.data_source.room.UserDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CahcheDatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : AppCacheDatabase{
        return Room.databaseBuilder(
            context,
            AppCacheDatabase::class.java,
            AppCacheDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(appCacheDatabase: AppCacheDatabase) : UserDao{
        return appCacheDatabase.userDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(appCacheDatabase: AppCacheDatabase) : CategoryDao{
        return appCacheDatabase.categoryDao()
    }

    @Singleton
    @Provides
    fun provideProductDao(appCacheDatabase: AppCacheDatabase) : ProductDao{
        return appCacheDatabase.productDao()
    }
}