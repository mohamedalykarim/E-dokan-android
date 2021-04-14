package mohalim.store.edokan.core.model.category

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryCache (

        @PrimaryKey
        @ColumnInfo(name = "category_id")
        var categoryId : Int,

        @ColumnInfo(name = "category_name")
        var categoryName : String,

        @ColumnInfo(name = "category_image")
        var categoryImage : String,

        @ColumnInfo(name = "category_parent")
        var categoryParent : Int
){
}