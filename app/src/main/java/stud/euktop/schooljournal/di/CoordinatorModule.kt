package stud.euktop.schooljournal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.schooljournal.presentation.auth.common.contract.AuthCoordinator
import stud.euktop.schooljournal.presentation.auth.common.impl.AuthCoordinatorImpl
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.impl.CoordinatorExecImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CoordinatorModule {
    @Binds
    @Singleton
    abstract fun bindsCoordinatorExec(coordinatorExecImpl: CoordinatorExecImpl): CoordinatorExec

    @Binds
    @Singleton
    abstract fun bindAuthCoordinator(impl: AuthCoordinatorImpl): AuthCoordinator
}