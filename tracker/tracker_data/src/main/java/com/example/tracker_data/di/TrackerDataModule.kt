package com.example.tracker_data.di

import android.app.Application
import androidx.room.Room
import com.example.tracker_data.local.TrackerDatabase
import com.example.tracker_data.remote.OpenFoodAPI
import com.example.tracker_data.repository.TrackerRepositoryImpl
import com.example.tracker_domain.repository.TrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TrackerDataModule {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            // Interceptor: Logging network requests
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenFoodAPI(client: OkHttpClient): OpenFoodAPI {
        return Retrofit.Builder()
            .baseUrl(OpenFoodAPI.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providesTrackerDatabase(app: Application): TrackerDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = TrackerDatabase::class.java,
            name = "tracker_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesTrackerRepository(
        db: TrackerDatabase,
        api: OpenFoodAPI
    ): TrackerRepository {
        return TrackerRepositoryImpl(
            dao = db.dao,   // Easy to test with a mock DB -> In-memory db with Room
            api = api
        )
    }
}