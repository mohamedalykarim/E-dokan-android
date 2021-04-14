package mohalim.store.edokan.core.model.category

data class Category (
        var categoryId : Int,
        var categoryName : String,
        var categoryImage : String,
        var categoryParent : Int
){
}