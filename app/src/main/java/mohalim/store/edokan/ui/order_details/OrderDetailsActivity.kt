package mohalim.store.edokan.ui.order_details

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
import mohalim.store.edokan.databinding.RowCartMarketplaceBinding
import mohalim.store.edokan.databinding.RowCartProductBinding
import mohalim.store.edokan.ui.product.ProductActivity

@AndroidEntryPoint
class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderDetailsBinding
    val viewModel: OrderViewModel by viewModels()
    val firebaseAuth = FirebaseAuth.getInstance()

    var orderId : Int = 0
    lateinit var orderProducts : List<OrderProduct>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details)

        if (!intent.hasExtra(Constants.constants.ORDER_ID)){
            finish()
            return
        }

        orderId = intent.getIntExtra(Constants.constants.ORDER_ID, 0)

        subscibeObservers()
    }

    override fun onResume() {
        super.onResume()

        firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
            viewModel.startGetOrderDetails(orderId, it.token.toString())
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

        }
    }

    private fun updateUI(order: Order) {
        /**
         * Single items
         */

        binding.orderNumber.text = "#"+order.order_id.toString()
        binding.orderValueTv.text = String.format("%.2f", order.value)
        binding.deliveryValueTv.text = String.format("%.2f", order.delivery_value)
        binding.totalTv.text = String.format("%.2f", order.value + order.delivery_value)
        binding.distanceTv.text = "Distance to deliver your order is "+ String.format("%.2f", (order.distance/1000)) + " km"
        binding.addressTV.text = order.address_line1 + ", "+ order.address_line2

        /**
         * Update marketplaces
          */
        binding.marketPlaceContainerContainer.removeAllViews()
        order.order_marketplaces.forEach {
            val marketplaceBinding : RowCartMarketplaceBinding = DataBindingUtil.inflate(
                LayoutInflater.from(this),
                R.layout.row_cart_marketplace,
                binding.marketPlaceContainerContainer,
                false
            )

            Log.d("TAG", "updateUI: "+ it.marketplace_name)

            marketplaceBinding.marketplaceNameTv.text = it.marketplace_name
            binding.marketPlaceContainerContainer.addView(marketplaceBinding.root)
        }

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