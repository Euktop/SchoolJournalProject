package stud.euktop.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.data.BuildConfig
import stud.euktop.data.mock.repository.*
import stud.euktop.data.repository.*
import stud.euktop.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    private fun <T> init(impl: T, mock: T): T = if (BuildConfig.USE_MOCK) mock else impl

    @Provides
    @Singleton
    fun provideLessonMarksRepository(
        impl: LessonMarksRepositoryImpl,
        mock: LessonMarksMockRepositoryImpl
    ): LessonMarksRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideAuditLogRepository(
        impl: AuditLogRepositoryImpl,
        mock: AuditMockLogRepositoryImpl
    ): AuditLogRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideDashboardRepository(
        impl: DashboardRepositoryImpl,
        mock: DashboardMockRepositoryImpl
    ): DashboardRepository = init(impl, mock)

    @Provides
    @Singleton
    fun provideGradeRepository(
        impl: GradeRepositoryImpl,
        mock: GradeMockRepositoryImpl,
    ): GradeRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideStudentRepository(
        impl: StudentRepositoryImpl,
        mock: StudentMockRepositoryImpl
    ): StudentRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideAuthRepository(
        impl: AuthRepositoryImpl,
        mock: AuthMockRepositoryImpl
    ): AuthRepository = init(impl = impl, mock = mock)

    // === Новые репозитории для административной части ===
    @Provides
    @Singleton
    fun provideUserAdminRepository(
        impl: UserAdminRepositoryImpl,
        mock: UserAdminMockRepositoryImpl
    ): UserAdminRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideSchoolAdminRepository(
        impl: SchoolAdminRepositoryImpl,
        mock: SchoolAdminMockRepositoryImpl
    ): SchoolAdminRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideClassAdminRepository(
        impl: ClassAdminRepositoryImpl,
        mock: ClassAdminMockRepositoryImpl
    ): ClassAdminRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideSubjectAdminRepository(
        impl: SubjectAdminRepositoryImpl,
        mock: SubjectAdminMockRepositoryImpl
    ): SubjectAdminRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideAssignmentAdminRepository(
        impl: AssignmentAdminRepositoryImpl,
        mock: AssignmentAdminMockRepositoryImpl
    ): AssignmentAdminRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideHomeworkRepository(
        impl: HomeworkRepositoryImpl,
        mock: HomeworkMockRepositoryImpl
    ): HomeworkRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideLessonRepository(
        impl: LessonRepositoryImpl,
        mock: LessonMockRepositoryImpl
    ): LessonRepository = init(impl = impl, mock = mock)

    @Provides
    @Singleton
    fun provideRoomAdminRepository(
        impl: RoomAdminRepositoryImpl,
        mock: RoomAdminMockRepositoryImpl
    ): RoomAdminRepository = init(impl = impl, mock = mock)
}