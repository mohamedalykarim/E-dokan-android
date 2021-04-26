package mohalim.store.edokan.core.model.product_image

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class ProductImageCacheMapper
@Inject constructor(): EntityMapper<ProductImageCache, ProductImage>{
    override fun mapFromEntity(entity: ProductImageCache): ProductImage {
        return ProductImage(
                id = entity.id,
                productId = entity.productId,
                productImage = entity.productImage
        )

    }

    override fun mapToEntity(domainModel: ProductImage): ProductImageCache {
        return ProductImageCache(
                id = domainModel.id,
                productId = domainModel.productId,
                productImage = domainModel.productImage
        )
    }

    fun mapFromEntityList(entities : List<ProductImageCache>) : List<ProductImage>{
        return entities.map { mapFromEntity(it) }
    }
}