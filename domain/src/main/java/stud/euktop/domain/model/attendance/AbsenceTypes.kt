package stud.euktop.domain.model.attendance

enum class AbsenceTypes {
    IRRESPECTABLE, ILL, RESPECTABLE, G2, G3, G4, G5;

    fun getGrade() = when (this) {
        G2 -> 2; G3 -> 3; G4 -> 4; G5 -> 5;
        else -> null
    }
}