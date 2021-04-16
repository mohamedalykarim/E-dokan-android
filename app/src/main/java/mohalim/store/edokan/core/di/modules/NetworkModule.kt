package mohalim.store.edokan.core.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mohalim.store.edokan.core.data_source.network.CategoryInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.UserInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.ProductInterfaceRetrofit
import mohalim.store.edokan.core.data_source.network.OfferInterfaceRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder() : Gson{
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun ProvideRetrofit (gson: Gson) : Retrofit.Builder{
        return Retrofit.Builder()
            .baseUrl("http:192.168.1.105:3000")
            .addConverterFactory(GsonConverterFactory.create())

    }

    @Singleton
    @Provides
    fun provideUserInterfaceRetrofit(retrofit: Retrofit.Builder): UserInterfaceRetrofit{
        return retrofit
                .build()
                .create(UserInterfaceRetrofit::class.java)

    }

    @Singleton
    @Provides
    fun provideCategoryInterfaceRetrofit(retrofit: Retrofit.Builder): CategoryInterfaceRetrofit{
        return retrofit
            .build()
            .create(CategoryInterfaceRetrofit::class.java)

    }

    @Singleton
    @Provides
    fun provideProductInterfaceRetrofit(retrofit: Retrofit.Builder): ProductInterfaceRetrofit {
        return retrofit
                .build()
                .create(ProductInterfaceRetrofit::class.java)

    }

    @Singleton
    @Provides
    fun provideOfferInterfaceRetrofit(retrofit: Retrofit.Builder): OfferInterfaceRetrofit {
        return retrofit
                .build()
                .create(OfferInterfaceRetrofit::class.java)

    }

}