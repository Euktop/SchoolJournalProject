package stud.euktop.schooljournal.presentation.common.contract.action

import stud.euktop.domain.model.school.School
import stud.euktop.domain.model.user.UserProfile

interface ClassFormActions {
    fun updateGrade(value: Int?)
    fun updateLetter(value: String)
    fun updateAcademicYearStart(year: Int?)
    fun updateAcademicYearEnd(year: Int?)
    fun updateSchool(school: School?)
    fun updateClassTeacher(teacher: UserProfile?)
}