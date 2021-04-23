package mohalim.store.edokan.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.databinding.FragmentCategoryBinding
import mohalim.store.edokan.databinding.RowCategoryCategoryItemBinding

class CategoryFragment : Fragment() {
    private var categoryId: Int = 0;
    lateinit var binding : FragmentCategoryBinding;
    private lateinit var categoryAdapter: CategoryAdapter;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        val activity = activity as CategoryActivity

        categoryAdapter = CategoryAdapter(ArrayList<Category>())
        val categoryLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryLayoutManager.reverseLayout = true
        binding.categoriesRV.layoutManager = categoryLayoutManager
        binding.categoriesRV.adapter = categoryAdapter

        activity.viewModel.getCategoryFromCacheById(categoryId)
        activity.viewModel.getCategoriesByParentId(categoryId)

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

    fun updateCategoriesRV(data: List<Category>) {
        categoryAdapter.categories = data
        categoryAdapter.notifyDataSetChanged()
    }


    class CategoryAdapter(var categories : List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val binding : RowCategoryCategoryItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_category_category_item,
                    parent,
                    false)

            return CategoryViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.bindItems(categories.get(position))
        }

        override fun getItemCount(): Int {
            return categories.size
        }

        class CategoryViewHolder(val binding : RowCategoryCategoryItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bindItems(category : Category){
                binding.textView19.text = category.categoryName
                binding.root.setOnClickListener(View.OnClickListener {
                    val intent : Intent = Intent(binding.root.context, CategoryActivity::class.java)
                    intent.putExtra(Constants.constants.CATEGORY_ID, category.categoryId)
                    it.context.startActivity(intent)

                })
            }
        }


    }


}