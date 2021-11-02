package mohalim.store.edokan.ui.order_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.model.product.Product
import mohalim.store.edokan.core.repository.OrderRepositoryImp
import mohalim.store.edokan.core.repository.ProductRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(private val orderRepository : OrderRepositoryImp, val productRepositoryImp: ProductRepositoryImp) : ViewModel(){

    private val _orderDetailsObserver = MutableLiveData<DataState<Order>>()
    val orderDetailsObserver get() = _orderDetailsObserver

    private val _orderNativeProductsObserver = MutableLiveData<DataState<List<Product>>>()
    val orderNativeProductsObserver get() = _orderNativeProductsObserver


    fun startGetOrderDetails(orderId: Int, fToken: String) {
        viewModelScope.launch {
            orderRepository.getOrderDetails(orderId, fToken).collect {
                _orderDetailsObserver.value = it
            }
        }
    }

    fun getProductsFromInternal(productsIds: MutableList<String>) {
        viewModelScope.launch {
            productRepositoryImp.getProductsFromInternal(productsIds).collect {
                _orderNativeProductsObserver.value = it
            }
        }
    }


}