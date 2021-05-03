package mohalim.store.edokan.core.model.user

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class UserCacheMapper
@Inject constructor(): EntityMapper<UserCache, User>{
    override fun mapFromEntity(entity: UserCache): User {
        return User(
            userId = entity.userId,
            phoneNumber = entity.phoneNumber,
            userName = entity.userName,
            email = entity.email,
            defaultAddress = entity.defaultAddress,
            defaultAddressId= entity.defaultAddressId,
            profileImage = entity.profileImage,
            wtoken = "",
        )

    }

    override fun mapToEntity(domainModel: User): UserCache {
        return UserCache(
            userId = domainModel.userId,
            phoneNumber = domainModel.phoneNumber,
            userName = domainModel.userName,
            email = domainModel.email,
            defaultAddress = domainModel.defaultAddress,
            defaultAddressId= domainModel.defaultAddressId,
            profileImage = domainModel.profileImage
        )
    }

    fun mapFromEntityList(entities : List<UserCache>) : List<User>{
        return entities.map { mapFromEntity(it) }
    }
}