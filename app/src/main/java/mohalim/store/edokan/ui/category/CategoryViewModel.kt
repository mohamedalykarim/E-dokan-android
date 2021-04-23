package mohalim.store.edokan.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.repository.CategoryRepositoryImp
import mohalim.store.edokan.core.repository.ProductRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel
@Inject
constructor(private val categoryRepository : CategoryRepositoryImp, private val productRepository: ProductRepositoryImp) : ViewModel() {
    var productRandomId : Int = 0;
    var productLimit : Int = 10;
    var productOffset : Int = 0;


    private val _category : MutableLiveData<DataState<Category>> = MutableLiveData()
    val category : LiveData<DataState<Category>> get() = _category


    private val _categories : MutableLiveData<DataState<List<Category>>> = MutableLiveData()
    val categories : LiveData<DataState<List<Category>>> get() = _categories

    private val _products : MutableLiveData<DataState<List<Product>>> = MutableLiveData()
    val products : LiveData<DataState<List<Product>>> get() = _products


    fun getCategoryFromCacheById(id : Int){
        viewModelScope.launch {
            categoryRepository.getCategoryFromCacheById(id)
                    .collect {
                        _category.value = it
                    }
        }
    }

    fun getCategoriesByParentId(parentId : Int){
        viewModelScope.launch {
            categoryRepository.getCategoriesByParentId(parentId).collect {
                _categories.value = it
            }
        }
    }

    fun getProductForCategory(categoryId: Int, randomId: Int, limit: Int, offset: Int){
        viewModelScope.launch {
            productRepository.getProductForCategory(categoryId, randomId, limit, offset).collect {
                _products.value = it
            }
        }
    }
}