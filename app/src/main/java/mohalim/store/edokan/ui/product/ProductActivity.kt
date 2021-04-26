package mohalim.store.edokan.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.databinding.ActivityProductBinding

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {

    val viewModel : ProductViewModel by viewModels()
    private lateinit var binding: ActivityProductBinding;
    private lateinit var productFragment: ProductFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)

        if (intent.hasExtra(Constants.constants.PRODUCT_ID)){
            productFragment = ProductFragment()
            productFragment.productId = intent.getIntExtra(Constants.constants.PRODUCT_ID,0)

                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add(binding.fragmentContainerView.id, productFragment)
                    }

            subscribeObserver()
        }



    }

    private fun subscribeObserver() {
        viewModel.productImagesObserver.observe(this, Observer {
            when (it) {
                is DataState.Loading -> {

                }

                is DataState.Success -> {
                    Log.d("TAG", "subscribeObserver: "+ it.data.size)
                    viewModel.productImages = it.data
                    productFragment.updateImages()
                }

                is DataState.Failure -> {
                }
            }
        })
    }
}