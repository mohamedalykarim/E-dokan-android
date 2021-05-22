package mohalim.store.edokan.core.model.address

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class AddressCacheMapper
@Inject constructor(): EntityMapper<AddressCache, Address>{
    override fun mapFromEntity(entity: AddressCache): Address {
        return Address(
            addressId = entity.addressId,
            userId = entity.userId,
            addressName = entity.addressName,
            addressLine1 = entity.addressLine1,
            addressLine2 = entity.addressLine2,
            city = entity.city,
            addressLat = entity.addressLat,
            addressLng = entity.addressLng
        )

    }

    override fun mapToEntity(domainModel: Address): AddressCache {
        return AddressCache(
            addressId = domainModel.addressId,
            userId = domainModel.userId,
            addressName = domainModel.addressName,
            addressLine1 = domainModel.addressLine1,
            addressLine2 = domainModel.addressLine2,
            city = domainModel.city,
            addressLat = domainModel.addressLat,
            addressLng = domainModel.addressLng
        )
    }

    fun mapFromEntityList(entities : List<AddressCache>) : List<Address>{
        return entities.map { mapFromEntity(it) }
    }
}