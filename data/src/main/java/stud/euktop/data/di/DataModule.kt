package stud.euktop.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import stud.euktop.data.local.storage.contract.TokenStorage
import stud.euktop.network.NetworkClient
import stud.euktop.network.NetworkConfig
import stud.euktop.network.api_api.SchoolJournalClientApi
import stud.euktop.network.interceptor.TokenProvider
import stud.euktop.network.util.Logger
import stud.euktop.network.util.logger
import stud.euktop.network.util.toSimpleTag
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
    fun provideNetworkConfig(): NetworkConfig {
        logger = object : Logger {
            override fun i(tag: String, action: String, data: String?) {
                stud.euktop.domain.utils.loger.logger?.i(tag, action, data)
            }

            override fun d(tag: String, action: String, data: String?) {
                stud.euktop.domain.utils.loger.logger?.d(tag, action, data)
            }

            override fun e(
                tag: String,
                action: String,
                throwable: Throwable?,
                data: String?
            ) {
                stud.euktop.domain.utils.loger.logger?.e(tag, action, throwable, data)
            }

        }
        stud.euktop.domain.utils.loger.logger?.i(toSimpleTag(), "INIT LOGGER")
        return NetworkConfig("https://192.168.0.109:7191/")
    }


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

    @Provides
    @Singleton
    fun provideGradesApi(client: NetworkClient) = client.gradeApi()
}