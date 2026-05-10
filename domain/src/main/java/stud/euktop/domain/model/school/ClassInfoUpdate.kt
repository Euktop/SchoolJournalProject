package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.Field

data class ClassInfoUpdate(
    val classId: Int,
    val schoolId: Field<Int> = Field(),
    val grade: Field<Int> = Field(),
    val letter: Field<String> = Field(),
    val academicYearStart: Field<Int> = Field(),
    val academicYearEnd: Field<Int> = Field(),
    val teacherId: Field<Int?> = Field()
)