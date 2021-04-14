package mohalim.store.edokan.core.model.category

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CategoryNetWork (
        @SerializedName("category_id")
        @Expose()
        var categoryId : Int,

        @SerializedName("category_name")
        @Expose()
        var categoryName : String,

        @SerializedName("category_image")
        @Expose()
        var categoryImage : String,

        @SerializedName("category_parent")
        @Expose()
        var categoryParent : Int
){
}