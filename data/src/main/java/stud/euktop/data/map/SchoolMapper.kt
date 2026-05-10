package stud.euktop.data.map

import com.schooljournal.model.*
import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.school.SchoolUpdate

internal fun GetSchoolsResult.toDomain(): School = School(
    schoolId = schoolId ?: 0,
    name = name ?: "",
    region = region ?: "",
    address = address ?: ""
)

internal fun GetSchoolByIdResult.toDomain(): School = School(
    schoolId = schoolId ?: 0,
    name = name ?: "",
    region = region ?: "",
    address = address ?: ""
)

internal fun School.toCreateRequest(): CreateSchoolRequest = CreateSchoolRequest(
    name = name,
    region = region,
    address = address,
    phone = null   // если поле phone добавите позже
)

internal fun SchoolUpdate.toUpdateRequest(): UpdateSchoolRequest = UpdateSchoolRequest(
    name = name.uValue,
    region = region.uValue,
    address = address.uValue,
    phone = null,
    updatePhone = false
)

internal fun CreateSchoolResult.toDomain(): School = School(
    schoolId = schoolId ?: 0,
    name = name ?: "",
    region = region ?: "",
    address = address ?: ""
)

internal fun UpdateSchoolResult.toDomain(): School = School(
    schoolId = schoolId ?: 0,
    name = name ?: "",
    region = region ?: "",
    address = address ?: ""
)