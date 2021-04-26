package mohalim.store.edokan.core.model.product_image

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "product_image_cache")
data class ProductImageCache(
        @PrimaryKey
        @ColumnInfo(name = "product_image_id")
        var id : Int,
        @ColumnInfo(name = "product_id")
        var productId : Int,
        @ColumnInfo(name = "product_image")
        var productImage : String
)
