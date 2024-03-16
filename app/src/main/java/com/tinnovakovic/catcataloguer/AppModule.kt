package com.tinnovakovic.catcataloguer

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room.databaseBuilder
import com.tinnovakovic.catcataloguer.data.CatRemoteMediator
import com.tinnovakovic.catcataloguer.data.TheCatApi
import com.tinnovakovic.catcataloguer.data.db.CatDatabase
import com.tinnovakovic.catcataloguer.data.mediator.CatImageRemoteMediatorFactory
import com.tinnovakovic.catcataloguer.data.mediator.CatImageRemoteMediatorFactoryImpl
import com.tinnovakovic.catcataloguer.data.models.db.CatEntity
import com.tinnovakovic.catcataloguer.shared.CAT_DATABASE
import com.tinnovakovic.catcataloguer.shared.PAGE_SIZE
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

    @OptIn(ExperimentalPagingApi::class)
    @Singleton
    @Provides
    fun provideCatPager(catDb: CatDatabase, catApi: TheCatApi): Pager<Int, CatEntity> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            remoteMediator = CatRemoteMediator(
                catDatabase = catDb,
                catApi = catApi
            ),
            pagingSourceFactory = {
                catDb.catDao().catPagingSource()
            }
        )
    }

    @Singleton
    @Provides
    fun provideCatDetailRemoteMediatorFactory(
        catDatabase: CatDatabase,
        catApi: TheCatApi
    ): CatImageRemoteMediatorFactory {
        return CatImageRemoteMediatorFactoryImpl(catDatabase, catApi)
    }

}
