package mohalim.store.edokan.core.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.utils.DataState

interface UserRepository {
    fun loginUserAfterPhone(user: FirebaseUser?, password: String) : Flow<DataState<User>>
    fun updateUserData(ftoken : String) : Flow<DataState<Boolean>>
}