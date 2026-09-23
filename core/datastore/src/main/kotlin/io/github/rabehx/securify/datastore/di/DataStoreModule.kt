package io.github.rabehx.securify.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.rabehx.securify.core.common.di.IoDispatcher
import io.github.rabehx.securify.datastore.UserPreferencesDataStore
import io.github.rabehx.securify.datastore.UserPreferencesRepository
import io.github.rabehx.securify.datastore.UserPreferencesSerializer
import io.github.rabehx.securify.datastore.model.UserPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        userPreferencesSerializer: UserPreferencesSerializer,
    ): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
            corruptionHandler = ReplaceFileCorruptionHandler { UserPreferences() },
            scope = CoroutineScope(ioDispatcher + SupervisorJob()),
        ) {
            context.dataStoreFile("user_preferences.pb")
        }
}

@Module
@InstallIn(SingletonComponent::class)
interface DataStoreBindingsModule {

    @Binds
    @Singleton
    fun bindUserPreferencesRepository(
        impl: UserPreferencesDataStore,
    ): UserPreferencesRepository
}
