package stud.euktop.schooljournal.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.domain.contract.RoleRepository
import stud.euktop.domain.impl.RoleRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRoleChecker(impl: RoleRepositoryImpl): RoleRepository
}