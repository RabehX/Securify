package io.github.rabehx.securify.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.rabehx.securify.BuildConfig
import io.github.rabehx.securify.network.NetworkConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppConfigModule {

    @Provides
    @Singleton
    fun provideNetworkConfig(): NetworkConfig = object : NetworkConfig {
        override val apiUrl: String = BuildConfig.API_URL
        override val isDebug: Boolean = BuildConfig.DEBUG
    }
}
