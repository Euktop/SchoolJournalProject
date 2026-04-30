package stud.euktop.domain.model.attendance

enum class AbsenceTypes {
    IRRESPECTABLE,
    ILL,
    RESPECTABLE,
    G2,
    G3,
    G4,
    G5;

    fun getGrade() = when (this) {
        AbsenceTypes.G2 -> 2
        AbsenceTypes.G3 -> 3
        AbsenceTypes.G4 -> 4
        AbsenceTypes.G5 -> 5
        else -> null
    }
}