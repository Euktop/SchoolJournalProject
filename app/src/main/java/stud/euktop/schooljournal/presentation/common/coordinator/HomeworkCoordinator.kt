package stud.euktop.schooljournal.presentation.common.coordinator

import stud.euktop.domain.model.homework.HomeworkFilter
import stud.euktop.domain.model.homework.HomeworkFull
import stud.euktop.domain.model.user.UserRef
import stud.euktop.domain.repository.HomeworkRepository
import stud.euktop.domain.repository.LessonRepository
import stud.euktop.domain.repository.UserAdminRepository
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeworkCoordinator @Inject constructor(
    private val homeworkRepository: HomeworkRepository,
    private val lessonRepository: LessonRepository,
    private val userRepository: UserAdminRepository,
    private val coordinatorExec: CoordinatorExec
) {

    suspend fun getHomeworkWithDetails(homeworkId: Int): CoordinatorResult<HomeworkFull> =
        coordinatorExec.exec {
            runCatching {
                val homework = homeworkRepository.getHomeworkById(homeworkId).getOrThrow()
                val lesson = lessonRepository.getLesson(homework.lessonId).getOrThrow()
                val createdBy = userRepository.getUser(homework.createdByUserId).getOrThrow()
                HomeworkFull(
                    homeworkId = homework.homeworkId,
                    lesson = lesson,
                    description = homework.description,
                    createdAt = homework.createdAt,
                    createdBy = UserRef.createFromUserProfile(createdBy),
                    media = homework.medias
                )
            }
        }

    suspend fun getHomeworksWithDetails(filter: HomeworkFilter): CoordinatorResult<List<HomeworkFull>> =
        coordinatorExec.exec {
            runCatching {
                val homeworks = homeworkRepository.getHomeworks(filter).getOrThrow()
                homeworks.map { homework ->
                    val lesson = lessonRepository.getLesson(homework.lessonId).getOrThrow()
                    val createdBy = userRepository.getUser(homework.createdByUserId).getOrThrow()
                    HomeworkFull(
                        homeworkId = homework.homeworkId,
                        lesson = lesson,
                        description = homework.description,
                        createdAt = homework.createdAt,
                        createdBy = UserRef.createFromUserProfile(createdBy),
                        media = homework.medias
                    )
                }
            }
        }
}