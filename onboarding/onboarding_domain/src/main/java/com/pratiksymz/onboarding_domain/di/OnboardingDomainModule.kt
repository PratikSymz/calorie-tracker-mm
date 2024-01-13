package com.pratiksymz.onboarding_domain.di

import com.pratiksymz.onboarding_domain.use_case.ValidateNutrients
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * Central place to define and store dependencies (objects, classes, etc.) needed within the Onboarding module
 * which other classes depend on. This is only used inside the ViewModel hence NO need for Singleton.
 */
@Module
@InstallIn(ViewModelComponent::class)
object OnboardingDomainModule {

    @Provides
    @ViewModelScoped
    fun provideValidateNutrientsUseCase(): ValidateNutrients {
        return ValidateNutrients()
    }
}