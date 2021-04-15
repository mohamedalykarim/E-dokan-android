package mohalim.store.edokan.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.databinding.FragmentHomeBinding
import mohalim.store.edokan.databinding.RowHomeCategoryBinding
import mohalim.store.edokan.databinding.RowHomeChosenProductsBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var categoryAdapter : CategoryAdapter;
    lateinit var chosenProductsAdapter: ChosenProductsAdapter;
    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val activity = activity as MainActivity
        chosenProductsAdapter = ChosenProductsAdapter(activity.viewmodel.products)
        categoryAdapter = CategoryAdapter(activity.viewmodel.categories)

        binding.categoriesRV.adapter = categoryAdapter;

        val categoryLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryLayoutManager.reverseLayout = true
        binding.categoriesRV.layoutManager = categoryLayoutManager



        binding.chosenRV.adapter = chosenProductsAdapter;

        val chosenLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        chosenLayoutManager.reverseLayout = true

        binding.chosenRV.layoutManager = chosenLayoutManager


        activity.viewmodel.fetchHomeFragmentData()


        return binding.root
    }

    fun updateCategoryData(data: List<Category>) {
        categoryAdapter.categories = data;
        categoryAdapter.notifyDataSetChanged()
    }

    fun updateChosenProductsData(data: List<Product>) {
        chosenProductsAdapter.products = data;
        chosenProductsAdapter.notifyDataSetChanged()
    }




    class CategoryAdapter(var categories : List<Category>) : RecyclerView.Adapter<CategoryAdapter.HomeCategoryRecyclerView>(){


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryRecyclerView {
            val binding : RowHomeCategoryBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_home_category,
                    parent,
                    false
            )

            return HomeCategoryRecyclerView(binding)
        }

        override fun onBindViewHolder(holder: HomeCategoryRecyclerView, position: Int) {
            holder.bindItems(categories[position])
        }

        override fun getItemCount(): Int {
            return categories.size
        }

        class HomeCategoryRecyclerView(val binding: RowHomeCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bindItems(category : Category){
                binding.categoryName.text = category.categoryName
                Glide.with(binding.root.context).load(Constants.constants.CATEGORY_IMAGE_BASE_URL + category.categoryImage).into(binding.imageView3)
            }

        }

    }

    class ChosenProductsAdapter(var products : List<Product>) : RecyclerView.Adapter<ChosenProductsAdapter.HomeChosenProductsRecyclerView>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeChosenProductsRecyclerView {
            val binding : RowHomeChosenProductsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_home_chosen_products,
                parent,
                false
            )

            return HomeChosenProductsRecyclerView(binding)
        }

        override fun onBindViewHolder(holder: HomeChosenProductsRecyclerView, position: Int) {
            holder.bindItems(products[position])
        }

        override fun getItemCount(): Int {
            return products.size
        }

        class HomeChosenProductsRecyclerView(val binding: RowHomeChosenProductsBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bindItems(product : Product){
                binding.priceTV.text = product.productPrice.toString()
                binding.productNameTV.text = product.productName
                binding.marketplaceTV.text = product.marketPlaceName
                Glide.with(binding.root.context).load(Constants.constants.PRODUCT_IMAGE_BASE_URL + product.productImage).into(binding.imageView4)
            }

        }

    }

}