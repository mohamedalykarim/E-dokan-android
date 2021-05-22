package mohalim.store.edokan.core.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohalim.store.edokan.core.data_source.room.*
import mohalim.store.edokan.core.data_source.room.OfferDao
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

    @Singleton
    @Provides
    fun provideOfferDao(appCacheDatabase: AppCacheDatabase) : OfferDao {
        return appCacheDatabase.offerDao()
    }

    @Singleton
    @Provides
    fun provideProductImagesDao(appCacheDatabase: AppCacheDatabase) : ProductImageDao{
        return appCacheDatabase.productImageDao()
    }

    @Singleton
    @Provides
    fun provideSupportItemDao(appCacheDatabase: AppCacheDatabase) : SupportItemDao{
        return appCacheDatabase.supportItemDao()
    }

    @Singleton
    @Provides
    fun provideSupportItemMessageDao(appCacheDatabase: AppCacheDatabase) : SupportItemMessageDao{
        return appCacheDatabase.supportItemMessageDao()
    }

    @Singleton
    @Provides
    fun provideCartProdcutDao(appCacheDatabase: AppCacheDatabase) : CartProductDao{
        return appCacheDatabase.cartProductDao()
    }

    @Singleton
    @Provides
    fun provideAddressDao(appCacheDatabase: AppCacheDatabase) : AddressDao{
        return appCacheDatabase.addressDao()
    }
}