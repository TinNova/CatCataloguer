package com.tinnovakovic.catcataloguer

import com.google.gson.Gson
import com.tinnovakovic.catcataloguer.data.api.TheCatApi
import com.tinnovakovic.catcataloguer.shared.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient.Builder,
        gsonConverterFactory: GsonConverterFactory
    ): TheCatApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(
                okHttpClient
                    .build()
            )
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
            .create(TheCatApi::class.java)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
    }

    @Provides
    @Singleton
    fun providesGsonConverter(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }
}

private const val BASE_URL = "https://api.thecatapi.com/v1/"
