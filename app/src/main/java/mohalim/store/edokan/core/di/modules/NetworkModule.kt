package mohalim.store.edokan.core.di.modules

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import mohalim.store.edokan.core.data_source.network.*
import mohalim.store.edokan.core.data_source.network.req.CityInterfaceRetrofit
import mohalim.store.edokan.core.utils.Constants
import okhttp3.OkHttpClient
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
    fun ProvideRetrofit (gson: Gson, @ApplicationContext context: Context) : Retrofit.Builder{
        val client = OkHttpClient.Builder()
                .addInterceptor(SignedRequestInterceptor(context))
                .build()


        return Retrofit.Builder()
            .baseUrl(Constants.constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
                .client(client)

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

    @Singleton
    @Provides
    fun provideSupportItemInterfaceRetrofit(retrofit: Retrofit.Builder): SupportItemInterfaceRetrofit {
        return retrofit
            .build()
            .create(SupportItemInterfaceRetrofit::class.java)

    }

    @Singleton
    @Provides
    fun provideCityInterfaceRetrofit(retrofit: Retrofit.Builder): CityInterfaceRetrofit {
        return retrofit
            .build()
            .create(CityInterfaceRetrofit::class.java)
    }

}