package com.pratiksymz.calorytracker.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.pratiksymz.core.data.preferences.DefaultPreferences
import com.pratiksymz.core.domain.preferences.Preferences
import com.pratiksymz.core.domain.use_case.FilterOutDigits
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Central place to define and store dependencies (objects, classes, etc.) needed throughout the application/project
 * which other classes depend on. Easily replaceable with other dependency for the entire app (central location).
 */
@Module
@InstallIn(SingletonComponent::class)   // Dependencies live as long as application does
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        app: Application
    ): SharedPreferences {
        return app.getSharedPreferences("shared_pref", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(
        sharedPreferences: SharedPreferences
    ): Preferences {
        // Can be swapped out with DataStorePreferences as well
        return DefaultPreferences(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideFilterOutDigitsUseCase(): FilterOutDigits {
        return FilterOutDigits()
    }
}