package mohalim.store.edokan.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {
    private var categoryId: Int = 0;
    lateinit var binding : FragmentCategoryBinding;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        val activity = activity as CategoryActivity

        activity.viewModel.getCategoryFromCacheById(categoryId)

        return binding.root
    }

    fun setCategoryId(categoryId : Int) {
        this.categoryId = categoryId
    }

    fun init(data: Category) {
        // collapsing toolbar image
        Glide.with(this)
                .load(Constants.constants.CATEGORY_IMAGE_BASE_URL+ data.categoryImage)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.bgImage)

        binding.categoryNameTv.text = data.categoryName

    }
}