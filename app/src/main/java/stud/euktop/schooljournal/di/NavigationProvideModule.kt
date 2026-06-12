package stud.euktop.schooljournal.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import stud.euktop.schooljournal.presentation.common.navigate.state.DestinationRoleChecker
import stud.euktop.schooljournal.presentation.common.navigate.state.NavigationStateManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NavigationProvideModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun provideDestinationRoleChecker(): DestinationRoleChecker = DestinationRoleChecker()

    @Provides
    @Singleton
    fun provideNavigationStateManager(
        dataStore: DataStore<Preferences>
    ): NavigationStateManager = NavigationStateManager(dataStore)
}