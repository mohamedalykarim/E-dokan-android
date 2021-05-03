package mohalim.store.edokan.core.repository

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import mohalim.store.edokan.core.data_source.network.UserInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.req.GetUserBody
import mohalim.store.edokan.core.data_source.room.UserDao
import mohalim.store.edokan.core.model.user.User
import mohalim.store.edokan.core.model.user.UserCacheMapper
import mohalim.store.edokan.core.model.user.UserNetwork
import mohalim.store.edokan.core.model.user.UserNetworkMapper
import mohalim.store.edokan.core.utils.DataState
import mohalim.store.edokan.core.utils.IPreferenceHelper
import mohalim.store.edokan.core.utils.PreferencesUtils
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


    override fun loginUserAfterPhone(user: FirebaseUser?, password: String): Flow<DataState<User>> {
        return flow {
            emit(DataState.Loading)
            try {
                val response : retrofit2.Response<UserNetwork> = retrofit.loginUserAfterPhone(
                    GetUserBody(
                        user?.uid,
                        user?.phoneNumber,
                        password
                    ), "Bearer " + preferenceHelper.getFirebaseToken()
                )
                val userNetwork : UserNetwork = response.body()!!
                val cookielist = response.headers().values("Set-Cookie")
                val refreshToken : String = cookielist.get(0).split(";")[0].substring(14)
                preferenceHelper.setRefreshToken(refreshToken)

                emit(DataState.Success(userNetworkMapper.mapFromEntity(userNetwork)))
            }catch (e: Exception){
                Log.d(TAG, "loginUserAfterPhone: " + e.message)
               emit(DataState.Failure(e))
            }
        }.flowOn(Dispatchers.IO)


    }


}