package mohalim.store.edokan.core.model.support_item

import mohalim.store.edokan.core.utils.EntityMapper
import java.math.BigInteger
import javax.inject.Inject

class SupportItemCacheMapper
@Inject constructor(): EntityMapper<SupportItemCache, SupportItem>{
    override fun mapFromEntity(entity: SupportItemCache): SupportItem {
        return SupportItem(
                supportItemId = entity.supportItemId,
                userId = entity.userId,
                supportItemStatus = entity.supportItemStatus,
                supportItemDate = entity.supportItemDate,
                message = entity.message
        )

    }

    override fun mapToEntity(domainModel: SupportItem): SupportItemCache {
        return SupportItemCache(
                supportItemId = domainModel.supportItemId,
                userId = domainModel.userId,
                supportItemStatus = domainModel.supportItemStatus,
                supportItemDate = domainModel.supportItemDate,
                message = domainModel.message
        )
    }

    fun mapFromEntityList(entities : List<SupportItemCache>) : List<SupportItem>{
        return entities.map { mapFromEntity(it) }
    }
}