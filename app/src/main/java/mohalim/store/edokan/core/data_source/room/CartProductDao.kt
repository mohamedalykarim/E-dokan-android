package mohalim.store.edokan.core.data_source.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mohalim.store.edokan.core.model.cart.CartProductCache
import mohalim.store.edokan.core.model.product.ProductCache

@Dao
interface CartProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartProductCache: CartProductCache) : Long

    @Query("SELECT * FROM cart_product")
    suspend fun getAll() : List<CartProductCache>

    @Query("SELECT * FROM cart_product WHERE product_id = :productId")
    suspend fun getProductById(productId : Int) : CartProductCache

    @Query("UPDATE cart_product SET product_count = (product_count + 1) WHERE product_id = :productId")
    suspend fun countUp(productId: Int) : Int

    @Query("UPDATE cart_product SET product_count = (product_count - 1) WHERE product_id = :productId AND product_count > 0")
    suspend fun countDown(productId: Int): Int

    @Query("DELETE FROM cart_product WHERE product_id = :productId")
    suspend fun remove(productId: Int): Int
}