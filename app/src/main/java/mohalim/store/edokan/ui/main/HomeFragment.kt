package mohalim.store.edokan.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.offer.Offer
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.viewpager.ZoomOutPageTransformer
import mohalim.store.edokan.databinding.*
import mohalim.store.edokan.ui.category.CategoryActivity
import mohalim.store.edokan.ui.product.ProductActivity

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var categoryAdapter : CategoryAdapter
    private lateinit var chosenProductsAdapter: ChosenProductsAdapter
    private lateinit var offersAdapter: HomeFragment.HomeFragmentSliderAdapter

    lateinit var inflater: LayoutInflater
    lateinit var container : ViewGroup

    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        this.inflater = inflater
        if (container != null) {
            this.container = container
        }

        val activity = activity as MainActivity
        activity.viewModel.fetchHomeFragmentData()

        initCategoryRV(activity)
        initChosenRV(activity)
        initSlider(activity,inflater,container)
        initSliderDots(inflater, container, 0)

        return binding.root
    }

    private fun initSliderDots(layoutInflater: LayoutInflater, container: ViewGroup?, index: Int) {
        binding.sliderDotContainer.removeAllViews()
        var i = 0
        offersAdapter.offers.forEach {
            if (i == index){
                val dotActiveBinding : SliderDotActiveBinding = DataBindingUtil.inflate(layoutInflater, R.layout.slider_dot_active, container, false)
                binding.sliderDotContainer.addView(dotActiveBinding.root)
            }else{
                val dotActiveBinding : SliderDotNotActiveBinding = DataBindingUtil.inflate(layoutInflater, R.layout.slider_dot_not_active, container, false)
                binding.sliderDotContainer.addView(dotActiveBinding.root)
            }
            i++
        }
    }

    private fun initChosenRV(activity: MainActivity) {
        chosenProductsAdapter = ChosenProductsAdapter(activity.viewModel.products)
        binding.chosenRV.adapter = chosenProductsAdapter
        val chosenLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        chosenLayoutManager.reverseLayout = true
        binding.chosenRV.layoutManager = chosenLayoutManager
    }

    private fun initCategoryRV(activity: MainActivity) {
        categoryAdapter = CategoryAdapter(activity.viewModel.categories)
        binding.categoriesRV.adapter = categoryAdapter
        val categoryLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryLayoutManager.reverseLayout = true
        binding.categoriesRV.layoutManager = categoryLayoutManager
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSlider(activity: MainActivity, inflater: LayoutInflater, container: ViewGroup?) {
        offersAdapter = HomeFragmentSliderAdapter(
                childFragmentManager,
                lifecycle,
                activity.viewModel.offers
        )
        binding.pager.adapter = offersAdapter
        binding.pager.setPageTransformer(ZoomOutPageTransformer())
        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                initSliderDots(inflater, container, position)
            }
        })
    }

    fun updateCategoryData(data: List<Category>) {
        categoryAdapter.categories = data
        categoryAdapter.notifyDataSetChanged()
    }

    fun updateChosenProductsData(data: List<Product>) {
        chosenProductsAdapter.products = data
        chosenProductsAdapter.notifyDataSetChanged()
    }

    fun updateOffersData(data: List<Offer>) {
        offersAdapter.offers = data
        offersAdapter.notifyDataSetChanged()
        initSliderDots(inflater, container, binding.pager.currentItem)
    }


    // Adapters

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
                Glide.with(binding.root.context)
                        .load(Constants.constants.CATEGORY_IMAGE_BASE_URL + category.categoryImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.imageView3)

                binding.root.setOnClickListener{
                    val intent = Intent(binding.root.context, CategoryActivity::class.java)
                    intent.putExtra(Constants.constants.CATEGORY_ID, category.categoryId)
                    it.context.startActivity(intent)
                }
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
                binding.priceTV.text = product.productPrice.toString()
                binding.productNameTV.text = product.productName
                binding.marketplaceTV.text = product.marketPlaceName
                Glide.with(binding.root.context)
                        .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + product.productImage)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.imageView4)

                binding.root.setOnClickListener{
                    val intent = Intent(binding.root.context, ProductActivity::class.java)
                    intent.putExtra(Constants.constants.PRODUCT_ID, product.productId)
                    binding.root.context.startActivity(intent)

                }
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
            return HomeSliderFragment(offers[position])
        }
    }

}