package mohalim.store.edokan.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohalim.store.edokan.core.data_source.network.CategoryInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.OfferInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.UserInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.ProductInterfaceRetrofit
import mohalim.store.edokan.core.data_source.room.CategoryDao
import mohalim.store.edokan.core.data_source.room.ProductDao
import mohalim.store.edokan.core.data_source.room.ProductImageDao
import mohalim.store.edokan.core.model.user.UserNetworkMapper
import mohalim.store.edokan.core.model.user.UserCacheMapper
import mohalim.store.edokan.core.data_source.room.UserDao
import mohalim.store.edokan.core.data_source.room.converter.OfferDao
import mohalim.store.edokan.core.model.category.CategoryCacheMapper
import mohalim.store.edokan.core.model.category.CategoryNetworkMapper
import mohalim.store.edokan.core.model.offer.OfferCacheMapper
import mohalim.store.edokan.core.model.offer.OfferNetworkMapper
import mohalim.store.edokan.core.model.product.ProductCacheMapper
import mohalim.store.edokan.core.model.product.ProductNetworkMapper
import mohalim.store.edokan.core.model.product_image.ProductImageCacheMapper
import mohalim.store.edokan.core.model.product_image.ProductImageNetworkMapper
import mohalim.store.edokan.core.model.product_rating.ProductRatingNetworkMapper
import mohalim.store.edokan.core.repository.CategoryRepositoryImp
import mohalim.store.edokan.core.repository.OfferRepositoryImp
import mohalim.store.edokan.core.repository.ProductRepositoryImp
import mohalim.store.edokan.core.repository.UserRepositoryImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(
            retrofit: UserInterfaceRetrofit,
            networkMapper: UserNetworkMapper,
            userDao: UserDao,
            cacheMapper: UserCacheMapper,
            @ApplicationContext context: Context
    ) : UserRepositoryImp{
        return UserRepositoryImp(
                retrofit,
                networkMapper,
                userDao,
                cacheMapper,
                context
        )
    }

    @Singleton
    @Provides
    fun provideCategoryRepository(
        retrofit: CategoryInterfaceRetrofit,
        networkMapper: CategoryNetworkMapper,
        categoryDao: CategoryDao,
        cacheMapper: CategoryCacheMapper,
        @ApplicationContext context: Context
    ) : CategoryRepositoryImp{
        return CategoryRepositoryImp(retrofit, networkMapper, categoryDao, cacheMapper, context)
    }

    @Singleton
    @Provides
    fun provideProductRepository(
            retrofit: ProductInterfaceRetrofit,
            networkMapper: ProductNetworkMapper,
            productDao: ProductDao,
            cacheMapper: ProductCacheMapper,
            productImageDao: ProductImageDao,
            productImageNetworkMapper: ProductImageNetworkMapper,
            productImageCacheMapper: ProductImageCacheMapper,
            productRatingNetworkMapper: ProductRatingNetworkMapper,
            @ApplicationContext context: Context
    ) : ProductRepositoryImp{
        return ProductRepositoryImp(
                retrofit,
                networkMapper,
                productDao,
                cacheMapper,
                productImageDao,
                productImageNetworkMapper,
                productImageCacheMapper,
                productRatingNetworkMapper,
                context
        )
    }

    @Singleton
    @Provides
    fun provideOfferRepository(
            retrofit: OfferInterfaceRetrofit,
            networkMapper: OfferNetworkMapper,
            offerDao: OfferDao,
            cacheMapper: OfferCacheMapper,
            @ApplicationContext context: Context
    ) : OfferRepositoryImp{
        return OfferRepositoryImp(retrofit, networkMapper, offerDao, cacheMapper, context)
    }

}