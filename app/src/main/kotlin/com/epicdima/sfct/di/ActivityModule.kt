package com.epicdima.sfct.di

import com.epicdima.sfct.exsearch.ParametersStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

/**
 * @author EpicDima
 */
@InstallIn(ActivityComponent::class)
@Module
object ActivityModule {
    @ActivityScoped
    @Provides
    fun provideParametersStorage() = ParametersStorage()
}