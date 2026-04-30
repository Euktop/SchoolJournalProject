package stud.euktop.domain.model.attendance

import java.util.Date

/**
 * Оценка/пропуск по предмету для детального экрана ученика.
 * @param date дата урока
 * @param absenceCode код прохождения
 * @param comment комментарий учителя
 */
data class StudentSubjectMark(
    val date: Date,
    val absenceCode: AbsenceTypes?,
    val comment: String?
)