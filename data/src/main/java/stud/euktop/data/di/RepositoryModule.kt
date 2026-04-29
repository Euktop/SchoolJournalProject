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
    @Binds
    @Singleton
    abstract fun bindsTeacherRepository(teacherRepositoryImpl: TeacherRepositoryImpl): TeacherRepository

    @Binds
    @Singleton
    abstract fun bindsTeacherLessonsRepository(impl: TeacherLessonsRepositoryImpl): TeacherLessonsRepository

    @Binds
    @Singleton
    abstract fun bindLessonMarksRepository(impl: LessonMarksRepositoryImpl): LessonMarksRepository

    @Binds
    @Singleton
    abstract fun bindAdminRepository(impl: AdminRepositoryImpl): AdminRepository

    @Binds
    @Singleton
    abstract fun bindStudentRepository(impl: StudentRepositoryImpl): StudentRepository


    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

}