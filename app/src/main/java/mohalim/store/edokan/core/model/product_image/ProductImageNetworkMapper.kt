package mohalim.store.edokan.core.model.product_image

import mohalim.store.edokan.core.utils.EntityMapper
import java.math.BigInteger
import javax.inject.Inject

class ProductImageNetworkMapper
@Inject constructor(): EntityMapper<ProductImageNetwork, ProductImage>{

    override fun mapFromEntity(entity: ProductImageNetwork): ProductImage {
        return ProductImage(
                id = entity.id,
                productId = entity.productId,
                productImage = entity.productImage
        )
    }

    override fun mapToEntity(domainModel: ProductImage): ProductImageNetwork {
        return ProductImageNetwork(
                id = domainModel.id,
                productId = domainModel.productId,
                productImage = domainModel.productImage
        )
    }

    fun mapFromEntityList(entities: List<ProductImageNetwork>) : List<ProductImage>{
        return entities.map { mapFromEntity(it) }
    }
}