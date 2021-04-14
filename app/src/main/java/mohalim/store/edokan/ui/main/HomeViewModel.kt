package mohalim.store.edokan.ui.main

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.repository.CategoryRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(private val savedStateHandle: SavedStateHandle,
                    private val categoryRepository: CategoryRepositoryImp) : ViewModel(){
    val HOME : String = "Home";
    val CART : String = "Cart";
    val ACCOUNT : String = "Account";

    val BOTTOM_HIDE = "bottom_hide"
    val BOTTOM_VISIBLE = "bottom_visible"

    var currentTab = HOME;
    var bottomVisibility = BOTTOM_VISIBLE;

    private val _noParentCategories : MutableLiveData<DataState<List<Category>>> = MutableLiveData()
    val noParentCategories : LiveData<DataState<List<Category>>> get() = _noParentCategories


    init {
        getNoParentCategories()
    }

    fun getNoParentCategories(){
       viewModelScope.launch {
           categoryRepository.getNoParentCategories().collect {
               _noParentCategories.value = it
           }
       }
   }

}