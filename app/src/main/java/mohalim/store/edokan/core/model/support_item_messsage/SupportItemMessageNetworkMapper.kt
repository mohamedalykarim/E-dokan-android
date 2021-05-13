package mohalim.store.edokan.core.model.support_item_messsage

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class SupportItemMessageNetworkMapper
@Inject constructor(): EntityMapper<SupportItemMessageNetwork, SupportItemMessage>{

    override fun mapFromEntity(entity: SupportItemMessageNetwork): SupportItemMessage {
        return SupportItemMessage(
            id = entity.id,
            supportItemId = entity.supportItemId,
            senderId = entity.senderId,
            message = entity.message,
            date = entity.date

        )
    }

    override fun mapToEntity(domainModel: SupportItemMessage): SupportItemMessageNetwork {
        return SupportItemMessageNetwork(
            id = domainModel.id,
            supportItemId = domainModel.supportItemId,
            senderId = domainModel.senderId,
            message = domainModel.message,
            date = domainModel.date
        )
    }

    fun mapFromEntityList(entities: List<SupportItemMessageNetwork>) : List<SupportItemMessage>{
        return entities.map { mapFromEntity(it) }
    }
}