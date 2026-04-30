package stud.euktop.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import stud.euktop.data.repository.*
import stud.euktop.domain.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // === Существующие репозитории (без изменений) ===
    @Binds
    @Singleton
    abstract fun bindsTeacherRepository(
        teacherRepositoryImpl: TeacherRepositoryImpl
    ): TeacherRepository

    @Binds
    @Singleton
    abstract fun bindsTeacherLessonsRepository(
        impl: TeacherLessonsRepositoryImpl
    ): TeacherLessonsRepository

    @Binds
    @Singleton
    abstract fun bindLessonMarksRepository(
        impl: LessonMarksRepositoryImpl
    ): LessonMarksRepository

    @Binds
    @Singleton
    abstract fun bindStudentRepository(
        impl: StudentRepositoryImpl
    ): StudentRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    // === Новые репозитории для административной части ===
    @Binds
    @Singleton
    abstract fun bindUserAdminRepository(
        impl: UserAdminRepositoryImpl
    ): UserAdminRepository

    @Binds
    @Singleton
    abstract fun bindSchoolAdminRepository(
        impl: SchoolAdminRepositoryImpl
    ): SchoolAdminRepository

    @Binds
    @Singleton
    abstract fun bindClassAdminRepository(
        impl: ClassAdminRepositoryImpl
    ): ClassAdminRepository

    @Binds
    @Singleton
    abstract fun bindSubjectAdminRepository(
        impl: SubjectAdminRepositoryImpl
    ): SubjectAdminRepository

    @Binds
    @Singleton
    abstract fun bindAssignmentAdminRepository(
        impl: AssignmentAdminRepositoryImpl
    ): AssignmentAdminRepository

    @Binds
    @Singleton
    abstract fun bindHomeworkRepository(impl: HomeworkRepositoryImpl): HomeworkRepository
}