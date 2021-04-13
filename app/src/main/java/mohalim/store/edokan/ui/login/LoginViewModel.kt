package mohalim.store.edokan.ui.login

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.repository.UserRepositoryImp
import mohalim.store.edokan.core.utils.DataState
import javax.inject.Inject


@HiltViewModel
class LoginViewModel
    @Inject constructor(
        private val savedStateHandle: SavedStateHandle,
        private val repositoryImp: UserRepositoryImp) : ViewModel() {


    private val _userDataState : MutableLiveData<DataState<User>> = MutableLiveData()
    val userDataState : LiveData<DataState<User>> get() = _userDataState

    fun loginAfterPhone(user: FirebaseUser?, password : String) {
        viewModelScope.launch {
            repositoryImp.loginUserAfterPhone(user, password)
                    .collect {
                       _userDataState.value = it
                    }
        }
    }
}