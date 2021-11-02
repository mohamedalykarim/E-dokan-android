package mohalim.store.edokan.ui.seller_order_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintProperties.*
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.model.order.OrderProduct
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.DensityUtil
import mohalim.store.edokan.databinding.ActivityOrderDetailsBinding
import mohalim.store.edokan.databinding.ActivitySellerOrderDetailsBinding
import mohalim.store.edokan.databinding.RowCartMarketplaceBinding
import mohalim.store.edokan.databinding.RowCartProductBinding
import mohalim.store.edokan.ui.product.ProductActivity

@AndroidEntryPoint
class SellerOrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerOrderDetailsBinding
    val viewModel: SellerOrderDetailsViewModel by viewModels()
    val firebaseAuth = FirebaseAuth.getInstance()

    var orderId : Int = 0
    var marketplaceId : Int = 0
    lateinit var orderProducts : List<OrderProduct>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_order_details)

        if (!intent.hasExtra(Constants.constants.ORDER_ID) || !intent.hasExtra(Constants.constants.MARKETPLACE_ID)){
            finish()
            return
        }



        orderId = intent.getIntExtra(Constants.constants.ORDER_ID, 0)
        marketplaceId = intent.getIntExtra(Constants.constants.MARKETPLACE_ID, 0)

        subscibeObservers()
    }

    override fun onResume() {
        super.onResume()

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            viewModel.startGetOrderDetails(orderId, marketplaceId, it.token.toString())
        }

    }

    private fun subscibeObservers() {
        viewModel.orderDetailsObserver.observe(this, Observer {
            when(it){
                is DataState.Loading -> {}
                is DataState.Success -> {
                    updateUI(it.data)
                }
                is DataState.Failure -> {
                    Log.d("TAG", "subscibeObservers: error "+ it.exception.message)
                }
            }
        })

        viewModel.orderNativeProductsObserver.observe(this, Observer {
            when(it){
                is DataState.Loading -> {}
                is DataState.Success -> {
                    updateProductUI(it.data)
                    Log.d("TAG", "subscibeObservers: "+ it.data)
                }
                is DataState.Failure -> { }
            }

        })
    }

    private fun updateProductUI(products: List<Product>) {
        var value = 0.0
        var discount = 0.0
        var total = 0.0

        binding.cartProductsContainer.removeAllViews()
        orderProducts.forEach{ orderProduct ->
            Log.d("TAG", "updateProductUI: orderProducts.foreach ")
            val cartProductBinding : RowCartProductBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.row_cart_product,
                null,
                false
            )

            var productName = ""
            var productDescription = ""
            var marketPlaceName = ""

            products.forEach{ product ->
                Log.d("TAG", "updateProductUI: "+product)
                if (product.productId == orderProduct.product_id){
                    productName = product.productName
                    productDescription = product.productDescription
                    Log.d("TAG", "updateProductUI: "+ product.marketPlaceName)
                    marketPlaceName = product.marketPlaceName
                }
            }


            // Update every item ui in product cart
            cartProductBinding.productTitle.text = productName
            var subDescription = ""
            if (productDescription.length > 20)  subDescription = productDescription.substring(0,20)
            cartProductBinding.descriptionTv.text = subDescription
            cartProductBinding.priceTv.text = (orderProduct.product_price - orderProduct.discount).toString()
            cartProductBinding.countTv.text = orderProduct.product_count.toString()
            cartProductBinding.marketplaceTv.text = marketPlaceName


            cartProductBinding.root.setOnClickListener {
                val intent = Intent(this, ProductActivity::class.java)
                intent.putExtra(Constants.constants.PRODUCT_ID, orderProduct.product_id)
                startActivity(intent)
            }

            val params = ConstraintLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            params.setMargins(
                DensityUtil.dipToPx(this, 16f),
                DensityUtil.dipToPx(this, 10f),
                DensityUtil.dipToPx(this, 16f),
                DensityUtil.dipToPx(this, 10f)
            )

            cartProductBinding.root.layoutParams = params

            binding.cartProductsContainer.addView(cartProductBinding.root)

            value += (orderProduct.product_count * orderProduct.product_price)
            discount += orderProduct.discount
            total += value
        }

        binding.orderValueTv.text = String.format("%.2f", value)
        binding.discountValueTv.text = String.format("%.2f", discount)
        binding.totalTv.text = String.format("%.2f", total)

    }

    private fun updateUI(order: Order) {
        /**
         * Single items
         */

        binding.orderNumber.text = "#"+order.order_id.toString()
        binding.distanceTv.text = "Distance to deliver your order is "+ String.format("%.2f", (order.distance/1000)) + " km"
        binding.addressTV.text = order.address_line1 + ", "+ order.address_line2



        /**
         * Start get Products from Internal storage
         */
        var productsIds : MutableList<String> = ArrayList()
        order.order_products.forEach{
            productsIds.add(it.product_id.toString())
        }
        orderProducts = order.order_products
        Log.d("TAG", "updateUI: "+productsIds.size)
        viewModel.getProductsFromInternal(productsIds)


    }
}