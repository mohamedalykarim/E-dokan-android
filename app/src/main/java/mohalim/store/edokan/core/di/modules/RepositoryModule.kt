package mohalim.store.edokan.core.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohalim.store.edokan.core.data_source.network.UserInterfaceRetrofit
import mohalim.store.edokan.core.model.user.UserNetworkMapper
import mohalim.store.edokan.core.model.user.UserCacheMapper
import mohalim.store.edokan.core.data_source.room.UserDao
import mohalim.store.edokan.core.repository.UserRepositoryImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideUserRepository(
        retrofit: UserInterfaceRetrofit,
        networkMapper: UserNetworkMapper,
        userDao: UserDao,
        cacheMapper: UserCacheMapper,
        @ApplicationContext context: Context
    ) : UserRepositoryImp{
        return UserRepositoryImp(retrofit, networkMapper, userDao, cacheMapper, context)
    }

}