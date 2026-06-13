package stud.euktop.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.data.local.storage.contract.RoleStorage
import stud.euktop.data.local.storage.contract.SettingsStorage
import stud.euktop.data.local.storage.contract.TokenStorage
import stud.euktop.data.local.storage.contract.UserIdStorage
import stud.euktop.data.local.storage.impl.SessionStorageImpl
import stud.euktop.data.local.storage.impl.SettingsStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class StorageModule {

    @Binds
    @Singleton
    abstract fun bindTokenStorage(impl: SessionStorageImpl): TokenStorage

    @Binds
    @Singleton
    abstract fun bindUserIdStorage(impl: SessionStorageImpl): UserIdStorage

    @Binds
    @Singleton
    abstract fun bindRoleStorage(impl: SessionStorageImpl): RoleStorage

    @Binds
    @Singleton
    abstract fun bindSettingsStorage(impl: SettingsStorageImpl): SettingsStorage
}