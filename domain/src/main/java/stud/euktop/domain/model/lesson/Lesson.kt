package stud.euktop.domain.model.lesson

import stud.euktop.domain.model.common.PrimaryKey
import stud.euktop.domain.model.user.UserInfo
import stud.euktop.domain.model.school.ClassInfo
import stud.euktop.domain.model.school.Room
import stud.euktop.domain.model.school.Subject
import stud.euktop.domain.utils.toBaseString
import java.util.Date

/**
 * Урок (расписание).
 *
 * @property lessonId уникальный идентификатор
 * @property classInfo класс, у которого проходит урок
 * @property subject предмет
 * @property teacher учитель
 * @property date дата урока
 * @property topic тема урока (опционально)
 * @property startTime время начала в формате "HH:mm"
 * @property endTime время окончания в формате "HH:mm"
 * @property room кабинет (опционально)
 * @property locationAddress адрес (если урок вне школы)
 */
data class Lesson(
    val lessonId: Int = 0,
    val classInfo: ClassInfo = ClassInfo(),
    val subject: Subject = Subject(),
    val teacher: UserInfo = UserInfo(),
    val date: Date = Date(),
    val topic: String? = null,
    val startTime: String = "",
    val endTime: String = "",
    val room: Room? = null,
    val locationAddress: String? = null
) : PrimaryKey<Int> {
    override val idKey: Int
        get() = lessonId
    val name = "${subject.name} - ${classInfo.name} (${date.toBaseString()})"
}