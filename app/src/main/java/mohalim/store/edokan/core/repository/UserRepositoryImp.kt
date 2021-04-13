package mohalim.store.edokan.core.repository

import android.content.Context
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.UserInterfaceRetrofit
import mohalim.store.edokan.core.model.user.UserNetworkMapper
import mohalim.store.edokan.core.data_source.network.req.GetUserBody
import mohalim.store.edokan.core.model.user.UserCacheMapper
import mohalim.store.edokan.core.data_source.room.UserDao
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
import java.lang.Exception
import javax.inject.Inject

class UserRepositoryImp
@Inject constructor(
    private val retrofit: UserInterfaceRetrofit,
    private val userNetworkMapper: UserNetworkMapper,
    private val userDao: UserDao,
    private val userCacheMapper: UserCacheMapper,
    context: Context
) : UserRepository {

    val TAG : String = "UserRepositoryImp"
    private val preferenceHelper: IPreferenceHelper by lazy { PreferencesUtils(context) }


    override fun loginUserAfterPhone(user : FirebaseUser?, password: String): Flow<DataState<User>> {
        val body : GetUserBody;
        if (password == ""){
            body = GetUserBody(user?.uid, user?.phoneNumber, "");
        }else {
            body = GetUserBody(user?.uid, user?.phoneNumber, password);

        }

        return flow {
            emit(DataState.Loading)
            try {
                val userNetwork = retrofit.loginUserAfterPhone(body, "Bearer "+ preferenceHelper.getFirebaseToken())
                emit(DataState.Success(userNetworkMapper.mapFromEntity(userNetwork)))
            }catch (e : Exception){
               emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)


    }


}