package mohalim.store.edokan.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.order.Order
import mohalim.store.edokan.core.repository.OrderRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(val orderRepository : OrderRepositoryImp) : ViewModel() {

    var limit = 10
    var offset = 0


    private val _ordersObserver : MutableLiveData<DataState<List<Order>>> = MutableLiveData()
    val ordersObserver : LiveData<DataState<List<Order>>> get() = _ordersObserver


    fun getOrders(limit: Int, offset: Int, fToken: String) {
        viewModelScope.launch {
            orderRepository.getOrders(limit, offset, fToken).collect {
                _ordersObserver.value = it
            }
        }
    }
}