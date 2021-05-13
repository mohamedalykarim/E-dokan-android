package mohalim.store.edokan.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.model.product_image.ProductImage
import mohalim.store.edokan.core.model.product_rating.ProductRating
import mohalim.store.edokan.core.repository.ProductRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class ProductViewModel
@Inject
constructor(val productRepository: ProductRepositoryImp) : ViewModel(){


    var productImages: List<ProductImage> = ArrayList();

    private val _similarProductObserver : MutableLiveData<DataState<List<Product>>> = MutableLiveData()
    val similarProductObserver : LiveData<DataState<List<Product>>> get() = _similarProductObserver


    private val _productImages : MutableLiveData<DataState<List<ProductImage>>> = MutableLiveData()
    val productImagesObserver : LiveData<DataState<List<ProductImage>>> get() = _productImages


    private val _productObserver : MutableLiveData<DataState<Product>> = MutableLiveData()
    val productObserver : LiveData<DataState<Product>> get() = _productObserver

    private val _productRatingObserver : MutableLiveData<DataState<ProductRating>> = MutableLiveData()
    val productRatingObserver : LiveData<DataState<ProductRating>> get() = _productRatingObserver


    fun getProductImages(productId : Int){
        viewModelScope.launch {
            productRepository.getProductImages(productId).collect {
                _productImages.value = it
            }
        }
    }

    fun getProductRating(productId : Int){
        viewModelScope.launch {
            productRepository.getProductRating(productId).collect {
                _productRatingObserver.value = it
            }
        }
    }

    fun getProductById(productId : Int){
        viewModelScope.launch {
            productRepository.getProductById(productId).collect {
                _productObserver.value = it
            }
        }
    }

    fun getSimilarProducts(cityId:Int, productName : String){
        viewModelScope.launch {
            productRepository.getSimilarProducts(cityId, productName).collect {
                _similarProductObserver.value = it
            }
        }
    }


}