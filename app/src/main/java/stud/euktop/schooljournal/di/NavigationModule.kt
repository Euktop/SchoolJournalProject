package stud.euktop.schooljournal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationRouterSplash
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.impl.NavigationManagerImpl
import stud.euktop.schooljournal.presentation.common.navigate.impl.NavigationRouterImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @Singleton
    abstract fun bindNavigationRouterSplash(navigationRouterImpl: NavigationRouterImpl): NavigationRouterSplash
}