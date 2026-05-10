// data/src/main/java/stud/euktop/data/mock/data/MockClassDataSource.kt
package stud.euktop.data.mock.data

import stud.euktop.domain.model.school.ClassInfo

internal object MockClassDataSource {
    private val _classes = mutableListOf<ClassInfo>().apply {
        val school1 = MockSchoolDataSource.getAll().find { it.schoolId == 1 }!!
        val school2 = MockSchoolDataSource.getAll().find { it.schoolId == 2 }!!
        val teacher1 = MockUserDataSource.getUser(2)  // Петрова
        val teacher2 = MockUserDataSource.getUser(1)  // Иванов

        add(
            ClassInfo(
                classId = 1,
                schoolId = school1.schoolId,
                grade = 5,
                letter = "А",
                academicYearStart = 2025,
                academicYearEnd = 2026,
                teacherId = teacher1?.userId
            )
        )
        add(
            ClassInfo(
                classId = 2,
                schoolId = school1.schoolId,
                grade = 5,
                letter = "Б",
                academicYearStart = 2025,
                academicYearEnd = 2026,
                teacherId = teacher2?.userId
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
        put(3, 1)  // Сидоров -> 5А
        put(4, 1)  // Борисова -> 5А
        put(5, 1)  // Дмитриева -> 5А
    }

    fun getClassByStudent(studentId: Int): ClassInfo? {
        val classId = _studentClassMap[studentId] ?: return null
        return get(classId)
    }

    fun setStudentClass(studentId: Int, classId: Int) {
        _studentClassMap[studentId] = classId
    }
}