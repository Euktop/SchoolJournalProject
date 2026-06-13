package stud.euktop.data.repository

import com.schooljournal.api.ClassesApi
import com.schooljournal.model.CreateClassRequest
import stud.euktop.data.map.toClassInfo
import stud.euktop.data.utils.ApiErrorHandler
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.ClassInfoFilter
import stud.euktop.domain.model.school.ClassInfoUpdate
import stud.euktop.domain.repository.ClassAdminRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassAdminRepositoryImpl @Inject constructor(
    private val classesApi: ClassesApi,
    private val errorHandler: ApiErrorHandler
) : ClassAdminRepository {

    override suspend fun getClasses(filter: ClassInfoFilter): Result<List<ClassInfo>> =
        errorHandler.safeApiCall {
            val dtos = classesApi.apiClassesFilterGet(
                schoolId = filter.schoolId,
                grade = null, // Можно добавить в фильтр при необходимости
                letter = null,
                query = filter.query,
                classTeacherId = filter.teacherId,
                filterByClassTeacherId = filter.teacherId != null,
                academicYearStart = filter.academicYearStart,
                offset = filter.pagination.offset,
                limit = filter.pagination.limit
            )
            dtos.map { it.toClassInfo() }
        }

    override suspend fun getClass(classId: Int): Result<ClassInfo> =
        errorHandler.safeApiCall {
            classesApi.apiClassesIdGet(classId).toClassInfo()
        }

    override suspend fun addClass(classInfo: ClassInfo): Result<ClassInfo> =
        errorHandler.safeApiCall {
            val request = CreateClassRequest(
                schoolId = classInfo.schoolId,
                grade = classInfo.grade,
                letter = classInfo.letter,
                academicYearStart = classInfo.academicYearStart,
                academicYearEnd = classInfo.academicYearEnd,
                classTeacherId = classInfo.teacherId
            )
            val result = classesApi.apiClassesPost(request)
            getClass(result.classId ?: 0).getOrThrow()
        }

    override suspend fun updateClass(update: ClassInfoUpdate): Result<ClassInfo> =
        errorHandler.safeApiCall {
            classesApi.apiClassesIdPatch(
                id = update.classId,
                schoolId = update.schoolId.uValue,
                updateSchoolId = update.schoolId.isUpdate,
                grade = update.grade.uValue,
                updateGrade = update.grade.isUpdate,
                letter = update.letter.uValue,
                updateLetter = update.letter.isUpdate,
                academicYearStart = update.academicYearStart.uValue,
                updateAcademicYearStart = update.academicYearStart.isUpdate,
                academicYearEnd = update.academicYearEnd.uValue,
                updateAcademicYearEnd = update.academicYearEnd.isUpdate,
                classTeacherId = update.teacherId.uValue,
                updateClassTeacherId = update.teacherId.isUpdate
            )
            getClass(update.classId).getOrThrow()
        }

    override suspend fun deleteClass(classId: Int): Result<Unit> =
        errorHandler.safeApiCall {
            classesApi.apiClassesIdDelete(classId)
        }
}