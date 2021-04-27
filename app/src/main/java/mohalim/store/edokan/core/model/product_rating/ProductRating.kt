package mohalim.store.edokan.core.model.product_rating

data class ProductRating(
        var productId : Int,
        var productRate : Float,
        var reviewCount : Int,
        var rating5Count : Int,
        var rating4Count : Int,
        var rating3Count : Int,
        var rating2Count : Int,
        var rating1Count : Int,
)
