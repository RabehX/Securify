package io.github.rabehx.securify.core.network.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.rabehx.securify.core.network.NetworkConfig
import io.github.rabehx.securify.core.network.api.GitHubApi
import io.github.rabehx.securify.core.network.api.IntegrityApi
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
        encodeDefaults = true
    }

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
    fun provideRetrofit(okHttp: OkHttpClient, config: NetworkConfig, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        val baseUrl = config.apiUrl.takeIf { it.isNotBlank() }?.let {
            if (it.endsWith("/")) it else "$it/"
        } ?: "https://localhost/"

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttp)
            .build()
    }

    @Provides
    @Singleton
    fun provideIntegrityApi(retrofit: Retrofit): IntegrityApi =
        retrofit.create(IntegrityApi::class.java)

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
