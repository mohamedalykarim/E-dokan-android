package mohalim.store.edokan.core.model.support_item

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class SupportItemNetworkMapper
@Inject constructor(): EntityMapper<SupportItemNetWork, SupportItem>{

    override fun mapFromEntity(entity: SupportItemNetWork): SupportItem {
        return SupportItem(
                supportItemId = entity.supportItemId,
                userId = entity.userId,
                supportItemStatus = entity.supportItemStatus,
                supportItemDate = entity.supportItemDate
        )
    }

    override fun mapToEntity(domainModel: SupportItem): SupportItemNetWork {
        return SupportItemNetWork(
                supportItemId = domainModel.supportItemId,
                userId = domainModel.userId,
                supportItemStatus = domainModel.supportItemStatus,
                supportItemDate = domainModel.supportItemDate
        )
    }

    fun mapFromEntityList(entities: List<SupportItemNetWork>) : List<SupportItem>{
        return entities.map { mapFromEntity(it) }
    }
}