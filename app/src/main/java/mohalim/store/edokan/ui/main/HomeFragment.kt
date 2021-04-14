package mohalim.store.edokan.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import mohalim.store.edokan.R
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.utils.Constants
import mohalim.store.edokan.databinding.FragmentHomeBinding
import mohalim.store.edokan.databinding.RowHomeCategoryBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    lateinit var categoryAdapter : CategoryAdapter;
    lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val activity = activity as MainActivity

        categoryAdapter = CategoryAdapter(activity.viewmodel.categories)
        binding.categoriesRV.adapter = categoryAdapter;

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.reverseLayout = true
        binding.categoriesRV.layoutManager = layoutManager


        return binding.root
    }

    fun updateCategoryData(data: List<Category>) {
        categoryAdapter.categories = data;
        categoryAdapter.notifyDataSetChanged()
    }


    class CategoryAdapter(var categories : List<Category>) : RecyclerView.Adapter<CategoryAdapter.HomeCategoryRecyclerView>(){


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

            fun bindItems(category : Category){
                binding.categoryName.text = category.categoryName
                Glide.with(binding.root.context).load(Constants.constants.CATEGORY_IMAGE_BASE_URL + category.categoryImage).into(binding.imageView3)
            }

        }

    }
}