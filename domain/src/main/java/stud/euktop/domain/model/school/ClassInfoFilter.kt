// domain/src/main/java/stud/euktop/domain/model/school/ClassInfoFilter.kt
package stud.euktop.domain.model.school

import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.user.UserInfoFilter

data class ClassInfoFilter(
    val school: School? = null,
    val schoolFilter: SchoolFilter = SchoolFilter(),
    val query: String? = null,          // поиск по строке, например "5А"
    val teacher: UserInfo? = null,
    val teacherFilter: UserInfoFilter = UserInfoFilter(),
    val academicYearStart: Int? = null,
    val academicYearEnd: Int? = null
)