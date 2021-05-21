package mohalim.store.edokan.core.model.cart

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class CartProductCacheMapper
@Inject constructor(): EntityMapper<CartProductCache, CartProduct>{
    override fun mapFromEntity(entity: CartProductCache): CartProduct {
        return CartProduct(
            productId = entity.productId,
            productName = entity.productName,
            productDescription = entity.productDescription,
            productImage = entity.productImage,
            productPrice = entity.productPrice,
            productDiscount = entity.productDiscount,
            marketPlaceId = entity.marketPlaceId,
            marketPlaceName = entity.marketPlaceName,
            marketPlaceLat = entity.marketPlaceLat,
            marketPlaceLng = entity.marketPlaceLng,
            productCount = entity.productCount

        )

    }

    override fun mapToEntity(domainModel: CartProduct): CartProductCache {
        return CartProductCache(
            productId = domainModel.productId,
            productName = domainModel.productName,
            productDescription = domainModel.productDescription,
            productImage = domainModel.productImage,
            productPrice = domainModel.productPrice,
            productDiscount = domainModel.productDiscount,
            marketPlaceId = domainModel.marketPlaceId,
            marketPlaceName = domainModel.marketPlaceName,
            marketPlaceLat = domainModel.marketPlaceLat,
            marketPlaceLng = domainModel.marketPlaceLng,
            productCount = domainModel.productCount
        )
    }

    fun mapFromEntityList(entities : List<CartProductCache>) : List<CartProduct>{
        return entities.map { mapFromEntity(it) }
    }
}