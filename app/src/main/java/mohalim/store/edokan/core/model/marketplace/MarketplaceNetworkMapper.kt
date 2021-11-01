package mohalim.store.edokan.core.model.marketplace

import mohalim.store.edokan.core.utils.EntityMapper
import java.math.BigInteger
import javax.inject.Inject

class MarketplaceNetworkMapper
@Inject constructor(): EntityMapper<MarketPlaceNetWork, MarketPlace>{
    override fun mapFromEntity(entity: MarketPlaceNetWork): MarketPlace {
        return MarketPlace(
            marketplaceId = entity.marketplaceId,
            marketplaceName = entity.marketplaceName,
            lat = entity.lat,
            lng = entity.lng,
            distanceToUser = 0f,
            cityId = entity.cityId,
            marketplaceOwnerId = entity.marketplaceOwnerId
        )

    }

    override fun mapToEntity(domainModel: MarketPlace): MarketPlaceNetWork {
        return MarketPlaceNetWork(
            marketplaceId = domainModel.marketplaceId,
            marketplaceName = domainModel.marketplaceName,
            lat = domainModel.lat,
            lng = domainModel.lng,
            cityId = domainModel.cityId,
            marketplaceOwnerId = domainModel.marketplaceOwnerId
        )
    }

    fun mapFromEntityList(entities : List<MarketPlaceNetWork>) : List<MarketPlace>{
        return entities.map { mapFromEntity(it) }
    }
}