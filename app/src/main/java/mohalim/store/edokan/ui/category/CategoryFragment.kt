package mohalim.store.edokan.ui.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.databinding.FragmentCategoryBinding
import mohalim.store.edokan.databinding.RowCategoryCategoryItemBinding
import mohalim.store.edokan.databinding.RowCategoryProductBinding

class CategoryFragment : Fragment() {
    val TAG : String = "CategoryFragment"
    private var categoryId: Int = 0;
    lateinit var binding : FragmentCategoryBinding;
    private lateinit var categoryAdapter: CategoryAdapter;
    private lateinit var productAdpater : ProductCategoryAdapter;

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
        binding.categoriesRV.setHasFixedSize(true)
        binding.categoriesRV.layoutManager = categoryLayoutManager
        binding.categoriesRV.adapter = categoryAdapter


        productAdpater = ProductCategoryAdapter(ArrayList<Product>())
        val productGridLayoutManager = GridLayoutManager(context, 3)
        binding.productsRV.setHasFixedSize(true)
        binding.productsRV.layoutManager = productGridLayoutManager
        binding.productsRV.adapter = productAdpater
        binding.productsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (productGridLayoutManager.findLastCompletelyVisibleItemPosition() == productAdpater.products.size - 1) {
                    activity.viewModel.getProductForCategory(
                            categoryId,
                            activity.viewModel.productRandomId,
                            activity.viewModel.productLimit,
                            activity.viewModel.productOffset
                    )

                    Log.d(TAG, "onScrolled: last")

                    activity.viewModel.productOffset = activity.viewModel.productOffset + activity.viewModel.productLimit
                }
            }
        })


        activity.viewModel.getCategoryFromCacheById(categoryId)
        activity.viewModel.getCategoriesByParentId(categoryId)

        val random = (1..1000).random()
        activity.viewModel.productRandomId = random

        activity.viewModel.getProductForCategory(
                categoryId,
                activity.viewModel.productRandomId,
                activity.viewModel.productLimit,
                activity.viewModel.productOffset
        )

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

    fun updateProductsAdapter(data: List<Product>) {
        productAdpater.products.addAll(data)
        productAdpater.notifyDataSetChanged()
    }

    class ProductCategoryAdapter(val products : MutableList<Product>) : RecyclerView.Adapter<ProductCategoryAdapter.CategoryProductViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
            val binding : RowCategoryProductBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_category_product,
                    parent,
                    false)

            return CategoryProductViewHolder(binding)
        }

        override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
            holder.bindItems(products.get(position))
        }

        override fun getItemCount(): Int {
            return products.size
        }

        class CategoryProductViewHolder(val binding : RowCategoryProductBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bindItems(product: Product) {
                Glide.with(binding.root.context)
                        .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + product.productImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.imageView3)

                binding.productNameTV2.text = product.productName
                binding.marketplaceTV.text = product.marketPlaceName
                binding.priceTV.text = "" + product.productPrice

            }
        }
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