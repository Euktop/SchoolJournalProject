package stud.euktop.data.mock

import stud.euktop.domain.model.school.School

object MockSchoolDataSource {
    private val _schools = mutableListOf<School>().apply {
        add(School(1, "Школа №1", "Регион 1", "Адрес 1"))
        add(School(2, "Школа №2", "Регион 2", "Адрес 2"))
        add(School(3, "Школа №3", "Регион 2", "Адрес 2"))
        add(School(4, "Школа №4", "Регион 2", "Адрес 2"))
        add(School(5, "Школа №5", "Регион 2", "Адрес 2"))
        add(School(6, "Школа №6", "Регион 2", "Адрес 2"))
        add(School(7, "Школа №7", "Регион 2", "Адрес 2"))
    }

    fun getAll(): List<School> = _schools.toList()
    fun add(school: School) { _schools.add(school) }
    fun update(school: School) {
        val index = _schools.indexOfFirst { it.schoolId == school.schoolId }
        if (index >= 0) _schools[index] = school
    }
    fun delete(schoolId: Int) { _schools.removeIf { it.schoolId == schoolId } }
}