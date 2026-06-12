package stud.euktop.schooljournal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.schooljournal.presentation.common.navigate.contract.ErrorHandler
import stud.euktop.schooljournal.presentation.common.navigate.contract.NavigationManager
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAuth
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterBack
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterError
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMain
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterMainMenu
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterProfile
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterSplash
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterStudent
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterTeacher
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

    @Binds
    @Singleton
    abstract fun bindsRouterBack(routerSplash: RouterImpl): RouterBack

    @Binds
    @Singleton
    abstract fun bindsRouterMain(routerMain: RouterImpl): RouterMain

    @Binds
    @Singleton
    abstract fun bindsRouterAuthorization(routerAuthorization: RouterImpl): RouterAuth

    @Binds
    @Singleton
    abstract fun bindsRouterAdmin(routerAdmin: RouterImpl): RouterAdmin

    @Binds
    @Singleton
    abstract fun bindsRouterError(routerError: RouterImpl): RouterError

    @Binds
    @Singleton
    abstract fun bindRouterMainMenu(impl: RouterImpl): RouterMainMenu

    @Binds
    @Singleton
    abstract fun bindRouterProfile(impl: RouterImpl): RouterProfile

    @Binds
    @Singleton
    abstract fun bindRouterStudent(impl: RouterImpl): RouterStudent

    @Binds
    @Singleton
    abstract fun bindRouterTeacher(impl: RouterImpl): RouterTeacher
}