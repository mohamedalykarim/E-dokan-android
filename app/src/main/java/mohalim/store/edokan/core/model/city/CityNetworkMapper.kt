package mohalim.store.edokan.core.model.city

import mohalim.store.edokan.core.utils.EntityMapper
import java.math.BigInteger
import javax.inject.Inject

class CityNetworkMapper
@Inject constructor(): EntityMapper<CityNetwork, City>{
    override fun mapFromEntity(entity: CityNetwork): City {
        return City(
            cityId = entity.cityId,
            cityName = entity.cityName
        )

    }

    override fun mapToEntity(domainModel: City): CityNetwork {
        return CityNetwork(
            cityId = domainModel.cityId,
            cityName = domainModel.cityName
        )
    }

    fun mapFromEntityList(entities : List<CityNetwork>) : List<City>{
        return entities.map { mapFromEntity(it) }
    }
}