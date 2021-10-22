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
            address_city = entity.address_city,
            city_name = entity.city_name,
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
            address_city = domainModel.address_city,
            city_name = domainModel.city_name,
            addressLat = domainModel.addressLat,
            addressLng = domainModel.addressLng
        )
    }

    fun mapFromEntityList(entities: List<AddressNetwork>) : List<Address>{
        return entities.map { mapFromEntity(it) }
    }
}