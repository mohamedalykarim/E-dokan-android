package mohalim.store.edokan.core.model.product_rating

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class ProductRatingNetworkMapper
@Inject constructor(): EntityMapper<ProductRatingNetwork, ProductRating>{

    override fun mapFromEntity(entity: ProductRatingNetwork): ProductRating {
        return ProductRating(
                productId = entity.productId,
                productRate = entity.productRate,
                reviewCount = entity.reviewCount,
                rating5Count = entity.rating5Count,
                rating4Count = entity.rating4Count,
                rating3Count = entity.rating3Count,
                rating2Count = entity.rating2Count,
                rating1Count = entity.rating1Count,
        )
    }

    override fun mapToEntity(domainModel: ProductRating): ProductRatingNetwork {
        return ProductRatingNetwork(
                productId = domainModel.productId,
                productRate = domainModel.productRate,
                reviewCount = domainModel.reviewCount,
                rating5Count = domainModel.rating5Count,
                rating4Count = domainModel.rating4Count,
                rating3Count = domainModel.rating3Count,
                rating2Count = domainModel.rating2Count,
                rating1Count = domainModel.rating1Count,
        )
    }

    fun mapFromEntityList(entities: List<ProductRatingNetwork>) : List<ProductRating>{
        return entities.map { mapFromEntity(it) }
    }
}