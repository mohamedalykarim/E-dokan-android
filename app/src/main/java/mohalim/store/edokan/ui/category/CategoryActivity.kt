package mohalim.store.edokan.ui.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.databinding.ActivityCategoryBinding

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {
    val TAG : String = "CategoryActivity"


    private lateinit var binding: ActivityCategoryBinding;
    private lateinit var cateFragment: CategoryFragment;
    val viewModel : CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)
        cateFragment = CategoryFragment();

        val intent : Intent = intent;
        if (intent.hasExtra(Constants.constants.CATEGORY_ID)){

             cateFragment.setCategoryId(intent.getIntExtra(Constants.constants.CATEGORY_ID,0))

            // load category fragment
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(binding.fragmentContainer.id, cateFragment,)
            }
        }

        subscribeObserver()

    }

    private fun subscribeObserver() {
        viewModel.category.observe(this, Observer {
            when (it) {
                is DataState.Loading -> {

                }

                is DataState.Success -> {
                    cateFragment.init(it.data)
                }

                is DataState.Failure -> {

                }
            }
        })
    }
}

