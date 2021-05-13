package mohalim.store.edokan.core.model.support_item_messsage

import mohalim.store.edokan.core.utils.EntityMapper
import java.math.BigInteger
import javax.inject.Inject

class SupportItemMessageCacheMapper
@Inject constructor(): EntityMapper<SupportItemMessageCache, SupportItemMessage>{
    override fun mapFromEntity(entity: SupportItemMessageCache): SupportItemMessage {
        return SupportItemMessage(
            id = entity.id,
            supportItemId = entity.supportItemId,
            senderId = entity.senderId,
            message = entity.message,
            date = entity.date
        )

    }

    override fun mapToEntity(domainModel: SupportItemMessage): SupportItemMessageCache {
        return SupportItemMessageCache(
            id = domainModel.id,
            supportItemId = domainModel.supportItemId,
            senderId = domainModel.senderId,
            message = domainModel.message,
            date = domainModel.date
        )
    }

    fun mapFromEntityList(entities : List<SupportItemMessageCache>) : List<SupportItemMessage>{
        return entities.map { mapFromEntity(it) }
    }
}