package mohalim.store.edokan.ui.product

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
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.cart.CartProduct
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.product_rating.ProductRating
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import mohalim.store.edokan.databinding.FragmentProductBinding
import mohalim.store.edokan.databinding.RowHomeChosenProductsBinding

@AndroidEntryPoint
class ProductFragment : Fragment() {

    var productId = 0
    lateinit var product : Product

    lateinit var binding : FragmentProductBinding
    lateinit var productActivity: ProductActivity
    private lateinit var similarProductAdapter : SimilarProductAdapter

    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(activity) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_product,
            container,
            false
        )

        productActivity = activity as ProductActivity
        productActivity.viewModel.getProductById(productId)
        productActivity.viewModel.getProductImages(productId)
        productActivity.viewModel.getProductRating(productId)
        productActivity.viewModel.getCartProductFromInternal(productId)

        imageClicks()
        initSimilarRV()

        binding.addBtn.setOnClickListener {
            if (preferenceHelper.getCartCityId() == -1 || preferenceHelper.getCartCityId() == preferenceHelper.getCityId() ){
                preferenceHelper.setCartCityId(preferenceHelper.getCityId()!!)

                val cartPrduct : CartProduct = CartProduct(
                    product.productId,
                    product.productName,
                    product.productDescription,
                    product.productImage,
                    product.productPrice,
                    product.productDiscount,
                    product.marketPlaceId,
                    product.marketPlaceName,
                    1
                )

                productActivity.viewModel.addProductToCart(cartPrduct)
            }else{
                //Todo no same city
            }
        }

        binding.countUpBtn.setOnClickListener {
            productActivity.viewModel.cartProdcutCountUpInternal(productId)
        }

        binding.countDownBtn.setOnClickListener {
            productActivity.viewModel.cartProdcutCountDownInternal(productId)
        }

        return binding.root
    }

    private fun initSimilarRV() {
        similarProductAdapter = SimilarProductAdapter(ArrayList())
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.reverseLayout = true
        binding.similarProductRV.layoutManager = layoutManager
        binding.similarProductRV.adapter = similarProductAdapter
    }

    private fun imageClicks() {
        binding.image1.setOnClickListener {
            if (productActivity.viewModel.productImages.isNotEmpty()){
                Glide.with(productActivity)
                        .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[0].productImage)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.bigImage)
            }
        }

        binding.image2.setOnClickListener{
            if (productActivity.viewModel.productImages.isNotEmpty()){
                Glide.with(productActivity)
                        .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[1].productImage)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.bigImage)
            }
        }

        binding.image3.setOnClickListener{
            if (productActivity.viewModel.productImages.isNotEmpty()){
                Glide.with(productActivity)
                        .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[2].productImage)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.bigImage)
            }
        }
    }

    fun updateImages() {
        Glide.with(productActivity)
                .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[0].productImage)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.bigImage)

        Glide.with(productActivity)
                .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[0].productImage)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.image1)

        Glide.with(productActivity)
                .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[1].productImage)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.image2)

        Glide.with(productActivity)
                .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[2].productImage)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(binding.image3)
    }

    fun updateProductData(product: Product) {
        this.product = product
        binding.productNameTV.text = product.productName
        binding.marketplaceNameTV.text = product.marketPlaceName
        binding.desciptionTv.text = product.productDescription
        binding.priceTV.text = product.productPrice.toString()

        productActivity.viewModel.getSimilarProducts(preferenceHelper.getCityId()!!, product.productName)
    }

    fun updateSimilarProductsData(data: List<Product>) {
        similarProductAdapter.products.addAll(data)
        similarProductAdapter.products.remove(product)
        similarProductAdapter.notifyDataSetChanged()
    }

    fun updateRating(data: ProductRating) {
        binding.rating.rating = data.productRate
        binding.ratingTv.text = data.productRate.toString()
    }

    fun cartProductAdded() {
        binding.addBtn.visibility = View.GONE
        binding.countCountainer.visibility = View.VISIBLE
    }

    fun getCartProduct(data: CartProduct) {
        if (data.productCount > 0){
            binding.addBtn.visibility = View.GONE
            binding.countCountainer.visibility = View.VISIBLE
            binding.cartCountTv.text = data.productCount.toString()
        }else{
            binding.addBtn.visibility = View.VISIBLE
            binding.countCountainer.visibility = View.GONE
            productActivity.viewModel.removeCartProduct(productId)
        }
    }

    class SimilarProductAdapter(var products : MutableList<Product>) : RecyclerView.Adapter<SimilarProductAdapter.SimilarProductViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarProductViewHolder {
            val binding : RowHomeChosenProductsBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.row_home_chosen_products,
                    parent,
                    false
            )

            return SimilarProductViewHolder(binding)
        }

        override fun onBindViewHolder(holder: SimilarProductViewHolder, position: Int) {
            holder.bindItem(products[position])
        }

        override fun getItemCount(): Int { return products.size }


        class SimilarProductViewHolder(val binding : RowHomeChosenProductsBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bindItem(product: Product) {
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
}