package stud.euktop.schooljournal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.data.repository.AuthRepositoryImpl
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.ErrorHandler
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterSplash
import stud.euktop.schooljournal.presentation.common.navigate.impl.CoordinatorExecImpl
import stud.euktop.schooljournal.presentation.common.navigate.impl.ErrorHandlerImpl
import stud.euktop.schooljournal.presentation.common.navigate.impl.NavigationManagerImpl
import stud.euktop.schooljournal.presentation.common.navigate.impl.RouterImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {
    @Binds
    @Singleton
    abstract fun bindsErrorHandler(errorHandlerImpl: ErrorHandlerImpl): ErrorHandler

    @Binds
    @Singleton
    abstract fun bindsNavigationManager(navigationManager: NavigationManagerImpl): NavigationManager

    @Binds
    @Singleton
    abstract fun bindsRouterSplash(routerSplash: RouterImpl): RouterSplash
}