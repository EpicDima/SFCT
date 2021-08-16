package com.epicdima.sfct.di

import com.epicdima.sfct.exsearch.ParametersStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author EpicDima
 */
@InstallIn(SingletonComponent::class)
@Module
object ParametersStorageModule {

    @Singleton
    @Provides
    fun provideParametersStorage() = ParametersStorage()
}