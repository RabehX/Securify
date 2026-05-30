package io.github.rabehx.securify.network.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.rabehx.securify.network.NetworkConfig
import io.github.rabehx.securify.network.api.GitHubApi
import io.github.rabehx.securify.network.api.IntegrityApi
import javax.inject.Named
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttp(config: NetworkConfig): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        if (config.isDebug) {
            val loggingInterceptor = HttpLoggingInterceptor { message ->
                Log.d("OkHttp", message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    @Named("integrity")
    fun provideIntegrityOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttp: OkHttpClient, config: NetworkConfig): Retrofit {
        return Retrofit.Builder()
            .baseUrl(config.apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
    }

    @Provides
    @Singleton
    fun provideIntegrityApi(
        @Named("integrity") okHttp: OkHttpClient,
        config: NetworkConfig,
    ): IntegrityApi {
        return Retrofit.Builder()
            .baseUrl(config.apiUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttp)
            .build()
            .create(IntegrityApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubApi(okHttp: OkHttpClient): GitHubApi {
        return Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/")
            .client(okHttp)
            .build()
            .create(GitHubApi::class.java)
    }
}
