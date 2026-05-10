package stud.euktop.domain.model.user

import stud.euktop.domain.model.common.Field
import java.util.Date

data class UserUpdate(
    val userId: Int,
    val lastName: Field<String> = Field(),
    val firstName: Field<String> = Field(),
    val surName: Field<String> = Field(),
    val gender: Field<Gender> = Field(),
    val birthday: Field<Date?> = Field(),
    val email: Field<String> = Field(),
    val phone: Field<String?> = Field(),
    val accountStatus: Field<AccountStatus> = Field()
)