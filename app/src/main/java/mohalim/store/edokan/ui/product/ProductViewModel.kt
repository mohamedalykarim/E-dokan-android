package mohalim.store.edokan.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.product_image.ProductImage
import mohalim.store.edokan.core.repository.ProductRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class ProductViewModel
@Inject
constructor(val productRepository: ProductRepositoryImp) : ViewModel(){


    var productImages: List<ProductImage> = ArrayList();

    private val _productImages : MutableLiveData<DataState<List<ProductImage>>> = MutableLiveData()
    val productImagesObserver : LiveData<DataState<List<ProductImage>>> get() = _productImages


    fun getProductImages(productId : Int){
        viewModelScope.launch {
            productRepository.getProductImages(productId).collect {
                _productImages.value = it
            }
        }
    }


}