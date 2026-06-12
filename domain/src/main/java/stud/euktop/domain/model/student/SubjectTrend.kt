package stud.euktop.domain.model.student

data class SubjectTrend(
    val value: Double, // изменение среднего балла (+0.12 или -0.05)
    val isPositive: Boolean,
    val formattedString: String // готовая строка для UI
)