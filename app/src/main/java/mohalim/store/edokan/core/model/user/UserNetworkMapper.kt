package mohalim.store.edokan.core.model.user

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class UserNetworkMapper
@Inject constructor(): EntityMapper<UserNetwork, User>{

    override fun mapFromEntity(entity: UserNetwork): User {
        return User(
            userId = entity.userId,
            phoneNumber = entity.phoneNumber,
            userName = entity.userName,
            email = entity.email,
            defaultAddress = entity.defaultAddress,
            defaultAddressId= entity.defaultAddressId,
            profileImage = entity.profileImage,
            wtoken = entity.wtoken
        )
    }

    override fun mapToEntity(domainModel: User): UserNetwork {
        return UserNetwork(
            userId = domainModel.userId,
            phoneNumber = domainModel.phoneNumber,
            userName = domainModel.userName,
            email = domainModel.email,
            defaultAddress = domainModel.defaultAddress,
            defaultAddressId= domainModel.defaultAddressId,
            profileImage = domainModel.profileImage,
            wtoken = domainModel.wtoken
        )
    }

    fun mapFromEntityList(entities: List<UserNetwork>) : List<User>{
        return entities.map { mapFromEntity(it) }
    }
}