package mohalim.store.edokan.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohalim.store.edokan.core.data_source.network.*
import mohalim.store.edokan.core.data_source.network.req.CityInterfaceRetrofit
import mohalim.store.edokan.core.data_source.room.*
import mohalim.store.edokan.core.model.user.UserNetworkMapper
import mohalim.store.edokan.core.model.user.UserCacheMapper
import mohalim.store.edokan.core.data_source.room.OfferDao
import mohalim.store.edokan.core.model.address.AddressCacheMapper
import mohalim.store.edokan.core.model.address.AddressNetworkMapper
import mohalim.store.edokan.core.model.cart.CartProductCacheMapper
import mohalim.store.edokan.core.model.category.CategoryCacheMapper
import mohalim.store.edokan.core.model.category.CategoryNetworkMapper
import mohalim.store.edokan.core.model.city.CityNetworkMapper
import mohalim.store.edokan.core.model.marketplace.MarketplaceCacheMapper
import mohalim.store.edokan.core.model.marketplace.MarketplaceNetworkMapper
import mohalim.store.edokan.core.model.offer.OfferCacheMapper
import mohalim.store.edokan.core.model.offer.OfferNetworkMapper
import mohalim.store.edokan.core.model.product.ProductCacheMapper
import mohalim.store.edokan.core.model.product.ProductNetworkMapper
import mohalim.store.edokan.core.model.product_image.ProductImageCacheMapper
import mohalim.store.edokan.core.model.product_image.ProductImageNetworkMapper
import mohalim.store.edokan.core.model.product_rating.ProductRatingNetworkMapper
import mohalim.store.edokan.core.model.support_item.SupportItemCacheMapper
import mohalim.store.edokan.core.model.support_item.SupportItemNetworkMapper
import mohalim.store.edokan.core.model.support_item_messsage.SupportItemMessageNetworkMapper
import mohalim.store.edokan.core.repository.*
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
            cartProductDao: CartProductDao,
            cartProductCacheMapper: CartProductCacheMapper,
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
                cartProductDao,
                cartProductCacheMapper,
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

    @Singleton
    @Provides
    fun provideSupportItemRepository(
        retrofit: SupportItemInterfaceRetrofit,
        networkMapper: SupportItemNetworkMapper,
        networkMessageMapper : SupportItemMessageNetworkMapper,
        dao : SupportItemDao,
        cacheMapper: SupportItemCacheMapper,
        @ApplicationContext context: Context
    ) : SupportItemRepositoryImp{
        return SupportItemRepositoryImp(retrofit, networkMapper, networkMessageMapper, dao, cacheMapper, context)
    }

    @Singleton
    @Provides
    fun provideCityRepository(
        retrofit: CityInterfaceRetrofit,
        networkMapper: CityNetworkMapper,
        @ApplicationContext context: Context
    ) : CityRepositoryImp{
        return CityRepositoryImp(retrofit, networkMapper, context)
    }

    @Singleton
    @Provides
    fun provideOrderRepository(
        retrofit: OrderInterfaceRetrofit,
        @ApplicationContext context: Context
    ) : OrderRepositoryImp{
        return OrderRepositoryImp(retrofit, context)
    }

    @Singleton
    @Provides
    fun provideAddressRepository(
        retrofit: AddressInterfaceRetrofit,
        networkMapper : AddressNetworkMapper,
        addressDao : AddressDao,
        cacheMapper: AddressCacheMapper,
        @ApplicationContext context: Context
    ) : AddressRepositoryImp{
        return AddressRepositoryImp(retrofit,networkMapper, addressDao, cacheMapper, context)
    }

    @Singleton
    @Provides
    fun provideSellerRepository(
        retrofit: SellerInterfaceRetrofit,
        networkMapper : MarketplaceNetworkMapper,
        marketplaceCacheMapper : MarketplaceCacheMapper,
        marketpalaceDao : MarketplaceDao,
        @ApplicationContext context: Context
    ) : SellerRepositoryImp{
        return SellerRepositoryImp(retrofit,networkMapper, marketplaceCacheMapper, marketpalaceDao, context)
    }

}