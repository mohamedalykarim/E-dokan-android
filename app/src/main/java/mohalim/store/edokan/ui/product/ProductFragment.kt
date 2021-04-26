package mohalim.store.edokan.ui.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.databinding.FragmentProductBinding

@AndroidEntryPoint
class ProductFragment : Fragment() {

    var productId = 0;

    lateinit var binding : FragmentProductBinding;
    lateinit var productActivity: ProductActivity;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_product,
            container,
            false
        )

        productActivity = activity as ProductActivity;
        productActivity.viewModel.getProductImages(productId)


        imageClicks()


        return binding.root
    }

    private fun imageClicks() {
        binding.image1.setOnClickListener(View.OnClickListener {
            if (productActivity.viewModel.productImages.isNotEmpty()){
                Glide.with(productActivity)
                        .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[0].productImage)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.bigImage)
            }
        })

        binding.image2.setOnClickListener(View.OnClickListener {
            if (productActivity.viewModel.productImages.isNotEmpty()){
                Glide.with(productActivity)
                        .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[1].productImage)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.bigImage)
            }
        })

        binding.image3.setOnClickListener(View.OnClickListener {
            if (productActivity.viewModel.productImages.isNotEmpty()){
                Glide.with(productActivity)
                        .load(Constants.constants.PRODUCT_IMAGE_BASE_URL + productActivity.viewModel.productImages[2].productImage)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(binding.bigImage)
            }
        })
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
}