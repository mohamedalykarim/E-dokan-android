package mohalim.store.edokan.core.model.category

import mohalim.store.edokan.core.utils.EntityMapper
import javax.inject.Inject

class CategoryCacheMapper
@Inject constructor(): EntityMapper<CategoryCache, Category>{
    override fun mapFromEntity(entity: CategoryCache): Category {
        return Category(
                categoryId =  entity.categoryId,
                categoryName = entity.categoryName,
                categoryImage = entity.categoryImage,
                categoryParent = entity.categoryParent
        )

    }

    override fun mapToEntity(domainModel: Category): CategoryCache {
        return CategoryCache(
            categoryId = domainModel.categoryId,
                categoryName = domainModel.categoryName,
                categoryImage = domainModel.categoryImage,
                categoryParent = domainModel.categoryParent
        )
    }

    fun mapFromEntityList(entities : List<CategoryCache>) : List<Category>{
        return entities.map { mapFromEntity(it) }
    }
}