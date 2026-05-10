package stud.euktop.domain.model.school

import stud.euktop.domain.model.common.Pagination

data class SubjectFilter(
    val name: String? = null,
    val pagination: Pagination = Pagination()
)