package mohalim.store.edokan.core.model.product

import mohalim.store.edokan.core.utils.EntityMapper
import java.math.BigInteger
import javax.inject.Inject

class ProductNetworkMapper
@Inject constructor(): EntityMapper<ProductNetwork, Product>{

    override fun mapFromEntity(entity: ProductNetwork): Product {
        return Product(
            productId = entity.productId,
            productName = entity.productName,
            productDescription = entity.productDescription,
            productImage = entity.productImage,
            productPrice = entity.productPrice,
            productDiscount = entity.productDiscount,
            productWeight = entity.productWeight,
            productLength = entity.productLength,
            productWidth = entity.productWidth,
            productHeight = entity.productHeight,
            marketPlaceId = entity.marketPlaceId,
            marketPlaceName = entity.marketPlaceName,
            marketPlaceLat = entity.marketPlaceLat,
            marketPlaceLng = entity.marketPlaceLng,
            marketPlaceCityId = entity.marketPlaceCityId,
            marketPlaceCityName = entity.marketPlaceCityName,
            productQuantity = entity.productQuantity,
            productViewed = entity.productViewed,
            productStatus = entity.productStatus,
            productChosen = entity.productChosen,
            dateAdded = entity.dateAdded,
            dateModified = entity.dateModified
        )
    }

    override fun mapToEntity(domainModel: Product): ProductNetwork {
        return ProductNetwork(
            productId = domainModel.productId,
            productName = domainModel.productName,
            productDescription = domainModel.productDescription,
            productImage = domainModel.productImage,
            productPrice = domainModel.productPrice,
            productDiscount = domainModel.productDiscount,
            productWeight = domainModel.productWeight,
            productLength = domainModel.productLength,
            productWidth = domainModel.productWidth,
            productHeight = domainModel.productHeight,
            marketPlaceId = domainModel.marketPlaceId,
            marketPlaceName = domainModel.marketPlaceName,
            marketPlaceLat = domainModel.marketPlaceLat,
            marketPlaceLng = domainModel.marketPlaceLng,
            marketPlaceCityId = domainModel.marketPlaceCityId,
            marketPlaceCityName = domainModel.marketPlaceCityName,
            productQuantity = domainModel.productQuantity,
            productViewed = domainModel.productViewed,
            productStatus = domainModel.productStatus,
            productChosen = domainModel.productChosen,
            dateAdded = domainModel.dateAdded,
            dateModified = domainModel.dateModified
        )
    }

    fun mapFromEntityList(entities: List<ProductNetwork>) : List<Product>{
        return entities.map { mapFromEntity(it) }
    }
}