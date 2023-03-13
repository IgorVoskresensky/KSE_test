package ru.ivos.kse_test.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ivos.kse_test.data.AppDatabase
import ru.ivos.kse_test.domain.dao.AppDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun getDatabase(context: Application): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun getDao(appDatabase: AppDatabase): AppDao {
        return appDatabase.getDao()
    }
}