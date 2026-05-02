package stud.euktop.domain.utils.validation

import stud.euktop.domain.model.user.UserInfo

class UserValidator(
    override var value: UserInfo?
) : Validator<UserInfo, UserValidator>() {

    override fun _validate(value: UserInfo?): Boolean {
        var result = false
        value?.apply {
            result = isAllValidate(
                NameLetterOnlyValidator(lastName),
                NameLetterOnlyValidator(firstName),
                NameLetterOnlyValidator(surName),
                EmailValidator(email),
                PhoneValidator(phone)
            )
        }
        return result
    }

    override fun getValidate(): UserInfo = value!!
    override fun copy(value: UserInfo?): UserValidator {
        return UserValidator(value)
    }
}