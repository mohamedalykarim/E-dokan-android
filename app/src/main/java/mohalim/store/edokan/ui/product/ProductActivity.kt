package mohalim.store.edokan.ui.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.databinding.ActivityProductBinding

@AndroidEntryPoint
class ProductActivity : AppCompatActivity() {

    val viewModel : ProductViewModel by viewModels()
    private lateinit var binding: ActivityProductBinding;
    private lateinit var productFragment: ProductFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product)

        productFragment = ProductFragment()

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(binding.fragmentContainerView.id, productFragment)
        }

    }
}