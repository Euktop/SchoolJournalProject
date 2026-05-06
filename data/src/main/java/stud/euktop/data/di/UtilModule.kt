package stud.euktop.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.data.utils.ApiErrorHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UtilModule {
    @Provides
    @Singleton
    fun provideApiErrorHandler() = ApiErrorHandler()
}