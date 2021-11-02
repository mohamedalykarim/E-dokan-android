package mohalim.store.edokan.core.model.marketplace

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class MarketplaceCacheMapper
@Inject constructor(): EntityMapper<MarketPlaceCache, MarketPlace>{
    override fun mapFromEntity(entity: MarketPlaceCache): MarketPlace {
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

    override fun mapToEntity(domainModel: MarketPlace): MarketPlaceCache {
        return MarketPlaceCache(
            marketplaceId = domainModel.marketplaceId,
            marketplaceName = domainModel.marketplaceName,
            lat = domainModel.lat,
            lng = domainModel.lng,
            cityId = domainModel.cityId,
            marketplaceOwnerId = domainModel.marketplaceOwnerId
        )
    }

    fun mapFromEntityList(entities : List<MarketPlaceCache>) : List<MarketPlace>{
        return entities.map { mapFromEntity(it) }
    }
}