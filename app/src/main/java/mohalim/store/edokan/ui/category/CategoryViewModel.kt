package mohalim.store.edokan.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.category.Category
import mohalim.store.edokan.core.repository.CategoryRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel
@Inject
constructor(private val categoryRepository : CategoryRepositoryImp) : ViewModel() {

    private val _category : MutableLiveData<DataState<Category>> = MutableLiveData()
    val category : LiveData<DataState<Category>> get() = _category


    public fun getCategoryFromCacheById(id : Int){
        viewModelScope.launch {
            categoryRepository.getCategoryFromCacheById(id)
                    .collect {
                        _category.value = it
                    }
        }
    }
}