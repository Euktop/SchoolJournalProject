package stud.euktop.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.network.NetworkClient
import stud.euktop.network.NetworkConfig
import stud.euktop.network.api.SchoolJournalClientApi
import stud.euktop.network.interceptor.TokenProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideToken() = TokenProvider { "" }

    @Provides
    @Singleton
    fun provideNetworkConfig() = NetworkConfig("https://192.168.0.109:7191/")

    @Provides
    @Singleton
    fun providePocketApi(
        tokenProvider: TokenProvider,
        networkConfig: NetworkConfig
    ) = SchoolJournalClientApi(NetworkClient(tokenProvider, networkConfig))
}