package stud.euktop.data.repository

import stud.euktop.data.MockData
import stud.euktop.domain.model.TeacherClassItem
import stud.euktop.domain.repository.TeacherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeacherRepositoryImpl @Inject constructor() : TeacherRepository {
    override suspend fun getTeacherClasses(): Result<List<TeacherClassItem>> {
        MockData.delay()
        return Result.success(MockData.classes)
    }
}