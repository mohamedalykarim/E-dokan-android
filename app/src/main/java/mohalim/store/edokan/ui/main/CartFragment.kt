package mohalim.store.edokan.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.cart.CartProduct
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.databinding.FragmentCartBinding
import mohalim.store.edokan.databinding.RowCartProductBinding
import mohalim.store.edokan.ui.product.ProductActivity

@AndroidEntryPoint
class CartFragment : Fragment() {
    var cartProducts: MutableList<CartProduct> = ArrayList()
    lateinit var binding : FragmentCartBinding

    lateinit var mainActivity: MainActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)

        mainActivity = activity as MainActivity

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainActivity.viewModel.getAllCartProductFromInternal()

    }

    fun updateProducts(data: List<CartProduct>) {
        binding.marketPlaceContainerContainer.removeAllViews()
        binding.cartProductsContainer.removeAllViews()

        data.forEach {
            val cartProductBinding : RowCartProductBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_cart_product,
                null,
                false
            )

            cartProductBinding.productTitle.text = it.productName
            val subDescription = it.productDescription.substring(0,25)
            cartProductBinding.descriptionTv.text = "$subDescription..."
            cartProductBinding.priceTv.text = (it.productPrice - it.productDiscount).toString()
            cartProductBinding.countTv.text = it.productCount.toString()


            val product = it
            cartProductBinding.root.setOnClickListener {
                val intent = Intent(mainActivity, ProductActivity::class.java)
                intent.putExtra(Constants.constants.PRODUCT_ID, product.productId)
                startActivity(intent)
            }

            binding.cartProductsContainer.addView(cartProductBinding.root)
        }
    }
}