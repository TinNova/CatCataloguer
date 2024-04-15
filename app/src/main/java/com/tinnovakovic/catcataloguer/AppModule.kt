package com.tinnovakovic.catcataloguer

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room.databaseBuilder
import com.tinnovakovic.catcataloguer.data.api.TheCatApi
import com.tinnovakovic.catcataloguer.data.db.CatDao
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import com.tinnovakovic.catcataloguer.data.mediator.CatImageRemoteMediatorFactory
import com.tinnovakovic.catcataloguer.data.mediator.CatImageRemoteMediatorFactoryImpl
import com.tinnovakovic.catcataloguer.data.mediator.CatRemoteMediatorFactory
import com.tinnovakovic.catcataloguer.data.mediator.CatRemoteMediatorFactoryImpl
import com.tinnovakovic.catcataloguer.shared.CAT_DATABASE
import com.tinnovakovic.catcataloguer.shared.ContextProvider
import com.tinnovakovic.catcataloguer.shared.ContextProviderImpl
import com.tinnovakovic.catcataloguer.shared.ExceptionHandler
import com.tinnovakovic.catcataloguer.shared.ExceptionHandlerImpl
import com.tinnovakovic.catcataloguer.shared.ResourceProvider
import com.tinnovakovic.catcataloguer.shared.ResourceProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCatDatabase(@ApplicationContext context: Context) =
        databaseBuilder(
            context = context,
            klass = CatDatabase::class.java,
            name = CAT_DATABASE
        )
            .build()

    @Singleton
    @Provides
    fun provideCatDAO(catDatabase: CatDatabase): CatDao {
        return catDatabase.catDao()
    }

    @Singleton
    @Provides
    fun provideCatRemoteMediatorFactory(
        catDatabase: CatDatabase,
        catApi: TheCatApi,
        exceptionHandler: ExceptionHandler
    ): CatRemoteMediatorFactory {
        return CatRemoteMediatorFactoryImpl(catDatabase, catApi, exceptionHandler)
    }

    @Singleton
    @Provides
    fun provideCatDetailRemoteMediatorFactory(
        catDatabase: CatDatabase,
        catApi: TheCatApi,
        exceptionHandler: ExceptionHandler
    ): CatImageRemoteMediatorFactory {
        return CatImageRemoteMediatorFactoryImpl(catDatabase, catApi, exceptionHandler)
    }

    @Provides
    @Singleton
    fun providesContextProvider(@ApplicationContext context: Context): ContextProvider =
        ContextProviderImpl(context)

    @Provides
    @Singleton
    fun providesResourceProvider(contextProvider: ContextProvider): ResourceProvider =
        ResourceProviderImpl(contextProvider)

    private const val DATA_STORE_NAME = "user_preferences"
    // Extension property to create a DataStore<Preferences> instance
    private val Context.dataStore by preferencesDataStore(name = DATA_STORE_NAME)

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        // Using DataStoreFactory for a custom initialization if needed
        return context.dataStore
    }

    @Singleton
    @Provides
    fun providesErrorHandler(contextProvider: ContextProvider): ExceptionHandler {
        return ExceptionHandlerImpl(contextProvider)
    }
}
