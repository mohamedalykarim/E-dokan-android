package mohalim.store.edokan.core.data_source.network.req

import com.google.gson.annotations.SerializedName

data class GetCategoriesByParentBody(
        @SerializedName("parent_id")
        var parentId : Int
)
