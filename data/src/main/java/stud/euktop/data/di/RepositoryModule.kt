package stud.euktop.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.data.BuildConfig
import stud.euktop.data.mock.repository.AssignmentAdminMockRepositoryImpl
import stud.euktop.data.mock.repository.AuthMockRepositoryImpl
import stud.euktop.data.mock.repository.ClassAdminMockRepositoryImpl
import stud.euktop.data.mock.repository.GradeMockRepositoryImpl
import stud.euktop.data.mock.repository.HomeworkMockRepositoryImpl
import stud.euktop.data.mock.repository.LessonMarksMockRepositoryImpl
import stud.euktop.data.mock.repository.LessonMockRepositoryImpl
import stud.euktop.data.mock.repository.RoomAdminMockRepositoryImpl
import stud.euktop.data.mock.repository.SchoolAdminMockRepositoryImpl
import stud.euktop.data.mock.repository.StudentMockRepositoryImpl
import stud.euktop.data.mock.repository.SubjectAdminMockRepositoryImpl
import stud.euktop.data.mock.repository.UserAdminMockRepositoryImpl
import stud.euktop.data.repository.AssignmentAdminRepositoryImpl
import stud.euktop.data.repository.AuthRepositoryImpl
import stud.euktop.data.repository.ClassAdminRepositoryImpl
import stud.euktop.data.repository.GradeRepositoryImpl
import stud.euktop.data.repository.HomeworkRepositoryImpl
import stud.euktop.data.repository.LessonMarksRepositoryImpl
import stud.euktop.data.repository.LessonRepositoryImpl
import stud.euktop.data.repository.RoomAdminRepositoryImpl
import stud.euktop.data.repository.SchoolAdminRepositoryImpl
import stud.euktop.data.repository.StudentRepositoryImpl
import stud.euktop.data.repository.SubjectAdminRepositoryImpl
import stud.euktop.data.repository.UserAdminRepositoryImpl
import stud.euktop.domain.repository.AssignmentAdminRepository
import stud.euktop.domain.repository.AuthRepository
import stud.euktop.domain.repository.ClassAdminRepository
import stud.euktop.domain.repository.GradeRepository
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.repository.LessonMarksRepository
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.domain.repository.RoomAdminRepository
import stud.euktop.domain.repository.SchoolAdminRepository
import stud.euktop.domain.repository.StudentRepository
import stud.euktop.domain.repository.SubjectAdminRepository
import stud.euktop.domain.repository.UserAdminRepository
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