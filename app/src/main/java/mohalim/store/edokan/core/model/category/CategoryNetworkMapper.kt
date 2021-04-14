package mohalim.store.edokan.core.model.category

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class CategoryNetworkMapper
@Inject constructor(): EntityMapper<CategoryNetWork, Category>{

    override fun mapFromEntity(entity: CategoryNetWork): Category {
        return Category(
                categoryId =  entity.categoryId,
                categoryName = entity.categoryName,
                categoryImage = entity.categoryImage,
                categoryParent = entity.categoryParent
        )
    }

    override fun mapToEntity(domainModel: Category): CategoryNetWork {
        return CategoryNetWork(
                categoryId = domainModel.categoryId,
                categoryName = domainModel.categoryName,
                categoryImage = domainModel.categoryImage,
                categoryParent = domainModel.categoryParent
        )
    }

    fun mapFromEntityList(entities: List<CategoryNetWork>) : List<Category>{
        return entities.map { mapFromEntity(it) }
    }
}