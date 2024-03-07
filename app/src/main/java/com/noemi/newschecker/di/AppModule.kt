package com.noemi.newschecker.di

import com.noemi.newschecker.helper.*
import com.noemi.newschecker.network.NewYorkTimesApi
import com.noemi.newschecker.repository.NewYorkTimesRepository
import com.noemi.newschecker.repository.NewYorkTimesRepositoryImpl
import com.noemi.newschecker.usecase.*
import com.noemi.newschecker.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Singleton
    @Provides
    fun providesHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun providesNewYorkTimesApi(client: OkHttpClient): NewYorkTimesApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create(NewYorkTimesApi::class.java)

    @Provides
    @Singleton
    fun providesApiResponseHelper(): ApiResponseHelper = ApiResponseHelperImpl()

    @Singleton
    @Provides
    fun providesDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesNewYorkTimesRepository(
        apiResponseHelper: ApiResponseHelper,
        newYorkTimesApi: NewYorkTimesApi,
        dispatcher: CoroutineDispatcher
    ): NewYorkTimesRepository = NewYorkTimesRepositoryImpl(apiResponseHelper, newYorkTimesApi, dispatcher)


    @Singleton
    @Provides
    fun providesGetMostPopularNewsUseCase(gitHubRepository: NewYorkTimesRepository): GetMostPopularNewsUseCase =
        GetMostPopularNewsUseCaseImpl(gitHubRepository)

}