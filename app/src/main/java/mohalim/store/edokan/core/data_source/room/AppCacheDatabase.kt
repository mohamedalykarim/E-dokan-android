package mohalim.store.edokan.core.data_source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import mohalim.store.edokan.core.data_source.room.converter.BigIntegerConverter
import mohalim.store.edokan.core.data_source.room.converter.OfferDao
import mohalim.store.edokan.core.model.category.CategoryCache
import mohalim.store.edokan.core.model.offer.OfferCache
import mohalim.store.edokan.core.model.product.ProductCache
import mohalim.store.edokan.core.model.product_image.ProductImageCache
import mohalim.store.edokan.core.model.support_item.SupportItemCache
import mohalim.store.edokan.core.model.user.UserCache

@Database(entities = [
                        UserCache::class,
                        CategoryCache::class,
                        ProductCache::class,
                        OfferCache::class,
                        ProductImageCache::class,
                        SupportItemCache::class
                     ],
        version = 1,
        exportSchema = false)
@TypeConverters(BigIntegerConverter::class)
abstract class AppCacheDatabase : RoomDatabase(){

    abstract fun userDao() : UserDao
    abstract fun categoryDao() : CategoryDao
    abstract fun productDao() : ProductDao
    abstract fun offerDao() : OfferDao
    abstract fun productImageDao() : ProductImageDao
    abstract fun supportItemDao() : SupportItemDao

    companion object{
        const val DATABASE_NAME = "e-dokan_app_database"
    }
}