package mohalim.store.edokan.core.model.address

import mohalim.store.edokan.core.utils.EntityMapper
import java.math.BigInteger
import javax.inject.Inject

class AddressNetworkMapper
@Inject constructor(): EntityMapper<AddressNetwork, Address>{

    override fun mapFromEntity(entity: AddressNetwork): Address {
        return Address(
            addressId = entity.addressId,
            userId = entity.userId,
            addressName = entity.addressName,
            addressLine1 = entity.addressLine1,
            addressLine2 = entity.addressLine2,
            city_id = entity.city_id,
            city = entity.city,
            addressLat = entity.addressLat,
            addressLng = entity.addressLng
        )
    }

    override fun mapToEntity(domainModel: Address): AddressNetwork {
        return AddressNetwork(
            addressId = domainModel.addressId,
            userId = domainModel.userId,
            addressName = domainModel.addressName,
            addressLine1 = domainModel.addressLine1,
            addressLine2 = domainModel.addressLine2,
            city_id = domainModel.city_id,
            city = domainModel.city,
            addressLat = domainModel.addressLat,
            addressLng = domainModel.addressLng
        )
    }

    fun mapFromEntityList(entities: List<AddressNetwork>) : List<Address>{
        return entities.map { mapFromEntity(it) }
    }
}