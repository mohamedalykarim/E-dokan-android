package mohalim.store.edokan.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.viewpager.ZoomOutPageTransformer
import mohalim.store.edokan.databinding.FragmentHomeBinding
import mohalim.store.edokan.databinding.RowHomeCategoryBinding
import mohalim.store.edokan.databinding.RowHomeChosenProductsBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {
    val TAG : String = "HomeFragment"

    private lateinit var categoryAdapter : CategoryAdapter;
    private lateinit var chosenProductsAdapter: ChosenProductsAdapter;
    private lateinit var offersAdapter: HomeFragment.HomeFragmentSliderAdapter

    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)


        val activity = activity as MainActivity
        activity.viewmodel.fetchHomeFragmentData()

        initCategoryRV(activity)
        initChosenRV(activity)
        initSlider(activity);


        return binding.root
    }

    private fun initChosenRV(activity: MainActivity) {
        chosenProductsAdapter = ChosenProductsAdapter(activity.viewmodel.products)
        binding.chosenRV.adapter = chosenProductsAdapter;
        val chosenLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        chosenLayoutManager.reverseLayout = true
        binding.chosenRV.layoutManager = chosenLayoutManager

    }

    private fun initCategoryRV(activity: MainActivity) {
        categoryAdapter = CategoryAdapter(activity.viewmodel.categories)
        binding.categoriesRV.adapter = categoryAdapter;
        val categoryLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryLayoutManager.reverseLayout = true
        binding.categoriesRV.layoutManager = categoryLayoutManager
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSlider(activity: MainActivity) {
        offersAdapter = HomeFragmentSliderAdapter(
                childFragmentManager,
                lifecycle,
                activity.viewmodel.offers
        );
        binding.pager.adapter = offersAdapter
        binding.pager.setPageTransformer(ZoomOutPageTransformer())
    }

    fun updateCategoryData(data: List<Category>) {
        categoryAdapter.categories = data;
        categoryAdapter.notifyDataSetChanged()
    }

    fun updateChosenProductsData(data: List<Product>) {
        chosenProductsAdapter.products = data;
        chosenProductsAdapter.notifyDataSetChanged()
        Log.d(TAG, "updateChosenProductsData: size"+chosenProductsAdapter.products.size)
    }

    fun updateOffersData(data: List<Offer>) {
        offersAdapter.offers = data
        offersAdapter.notifyDataSetChanged()
    }

    class CategoryAdapter(var categories: List<Category>) : RecyclerView.Adapter<CategoryAdapter.HomeCategoryRecyclerView>(){


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

            fun bindItems(category: Category){
                binding.categoryName.text = category.categoryName
                Glide.with(binding.root.context).load(Constants.constants.CATEGORY_IMAGE_BASE_URL + category.categoryImage).into(binding.imageView3)
            }

        }

    }

    class ChosenProductsAdapter(var products: List<Product>) : RecyclerView.Adapter<ChosenProductsAdapter.HomeChosenProductsRecyclerView>(){
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

            fun bindItems(product: Product){
                Log.d("TAG", "bindItems: price" + product.productPrice)
                binding.priceTV.text = product.productPrice.toString()
                binding.productNameTV.text = product.productName
                binding.marketplaceTV.text = product.marketPlaceName
                Glide.with(binding.root.context).load(Constants.constants.PRODUCT_IMAGE_BASE_URL + product.productImage).into(binding.imageView4)
            }

        }

    }


    private inner class HomeFragmentSliderAdapter constructor(
            fm: FragmentManager,
            lifecycle: Lifecycle,
            var offers : List<Offer>
    ) : FragmentStateAdapter(fm, lifecycle){


        override fun getItemCount(): Int = offers.size

        override fun createFragment(position: Int): Fragment {
            return HomeSliderFragment(offers.get(position))
        }
    }

}