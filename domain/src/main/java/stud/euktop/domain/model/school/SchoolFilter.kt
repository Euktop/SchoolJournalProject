package stud.euktop.domain.model.school

/**
 * Школа (учебное заведение).
 */
data class SchoolFilter(
    val name: String? = null,
    val region: String? = null,
    val address: String? = null
) {
/*    companion object {
        fun exec(school: School?) = SchoolFilter(school?.name, school?.region, school?.address)
    }*/
}