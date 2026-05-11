package stud.euktop.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import stud.euktop.data.local.storage.contract.TokenStorage
import stud.euktop.network.NetworkClient
import stud.euktop.network.NetworkConfig
import stud.euktop.network.api.SchoolJournalClientApi
import stud.euktop.network.interceptor.TokenProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    @Singleton
    fun provideToken(tokenStorage: TokenStorage): TokenProvider = TokenProvider {
        runBlocking { tokenStorage.getToken() }
    }

    @Provides
    @Singleton
    fun provideNetworkConfig() = NetworkConfig("https://192.168.1.136:7191/")


    @Provides
    @Singleton
    fun providePocketApi(
        tokenProvider: TokenProvider,
        networkConfig: NetworkConfig
    ) = SchoolJournalClientApi(NetworkClient(tokenProvider, networkConfig))

    @Provides
    @Singleton
    fun provideAuthorizationApi(client: NetworkClient) = client.authorizationApi()

    @Provides
    @Singleton
    fun provideClassesApi(client: NetworkClient) = client.classesApi()

    @Provides
    @Singleton
    fun provideHomeworkApi(client: NetworkClient) = client.homeworkApi()

    @Provides
    @Singleton
    fun provideLessonsApi(client: NetworkClient) = client.lessonsApi()

    @Provides
    @Singleton
    fun provideSchoolsApi(client: NetworkClient) = client.schoolsApi()

    @Provides
    @Singleton
    fun provideStudentApi(client: NetworkClient) = client.studentApi()

    @Provides
    @Singleton
    fun provideSubjectsApi(client: NetworkClient) = client.subjectsApi()

    @Provides
    @Singleton
    fun provideTeacherAssignmentsApi(client: NetworkClient) = client.teacherAssignmentsApi()

    @Provides
    @Singleton
    fun provideUsersApi(client: NetworkClient) = client.usersApi()

    @Provides
    @Singleton
    fun provideAuditApi(client: NetworkClient) = client.auditApi()

    @Provides
    @Singleton
    fun provideRolesApi(client: NetworkClient) = client.rolesApi()

    @Provides
    @Singleton
    fun provideAbsenceTypesApi(client: NetworkClient) = client.absenceTypesApi()

    @Provides
    @Singleton
    fun provideRoomsApi(client: NetworkClient) = client.roomsApi()
}