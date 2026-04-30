package stud.euktop.data.mock

import stud.euktop.domain.model.school.ClassInfo

object MockClassDataSource {
    private val _classes = mutableListOf<ClassInfo>().apply {
        val school1 = MockSchoolDataSource.getAll().find { it.schoolId == 1 }!!
        val school2 = MockSchoolDataSource.getAll().find { it.schoolId == 2 }!!
        val teacher1 = MockUserDataSource.getUser(2)  // Петрова
        val teacher2 = MockUserDataSource.getUser(1)  // Иванов

        add(
            ClassInfo(
                classId = 1,
                school = school1,
                grade = 5,
                letter = "А",
                academicYearStart = 2025,
                academicYearEnd = 2026,
                teacher = teacher1
            )
        )
        add(
            ClassInfo(
                classId = 2,
                school = school1,
                grade = 5,
                letter = "Б",
                academicYearStart = 2025,
                academicYearEnd = 2026,
                teacher = teacher2
            )
        )
    }

    fun getAll(): List<ClassInfo> = _classes.toList()
    fun get(classId: Int): ClassInfo? = _classes.find { it.classId == classId }
    fun add(classInfo: ClassInfo): ClassInfo {
        val newId = (_classes.maxOfOrNull { it.classId } ?: 0) + 1
        val newClass = classInfo.copy(classId = newId)
        _classes.add(newClass)
        return newClass
    }

    fun update(classInfo: ClassInfo) {
        val index = _classes.indexOfFirst { it.classId == classInfo.classId }
        if (index >= 0) _classes[index] = classInfo
    }

    fun delete(classId: Int): Boolean = _classes.removeIf { it.classId == classId }
    private val _studentClassMap = mutableMapOf<Int, Int>().apply {
        // Ученики с userId 3,4,5 относятся к классу 1 (5А)
        put(3, 1)
        put(4, 1)
        put(5, 1)
    }

    fun getClassByStudent(studentId: Int): ClassInfo? {
        val classId = _studentClassMap[studentId] ?: return null
        return get(classId)
    }

    // Опционально: метод для установки/изменения привязки
    fun setStudentClass(studentId: Int, classId: Int) {
        _studentClassMap[studentId] = classId
    }
}