package mohalim.store.edokan.core.model.offer

import mohalim.store.edokan.core.utils.EntityMapper
import java.math.BigInteger
import javax.inject.Inject

class OfferNetworkMapper
@Inject constructor(): EntityMapper<OfferNetwork, Offer>{
    override fun mapFromEntity(entity: OfferNetwork): Offer {
        return Offer(
                offerId = entity.offerId,
                offerType = entity.offerType,
                productId = entity.productId,
                offerImage = entity.offerImage,
                offerName = entity.offerName,
                offerDescription = entity.offerDescription,
                offerStatus = entity.offerStatus,
                offerEndDate = entity.offerEndDate,
                addedDate = entity.addedDate,
                modifiedDate = entity.modifiedDate
        )

    }

    override fun mapToEntity(domainModel: Offer): OfferNetwork {
        return OfferNetwork(
                offerId = domainModel.offerId,
                offerType = domainModel.offerType,
                productId = domainModel.productId,
                offerImage = domainModel.offerImage,
                offerName = domainModel.offerName,
                offerDescription = domainModel.offerDescription,
                offerStatus = domainModel.offerStatus,
                offerEndDate = domainModel.offerEndDate,
                addedDate = domainModel.addedDate,
                modifiedDate = domainModel.modifiedDate
        )
    }

    fun mapFromEntityList(entities : List<OfferNetwork>) : List<Offer>{
        return entities.map { mapFromEntity(it) }
    }
}