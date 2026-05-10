package stud.euktop.domain.utils.validation

import stud.euktop.domain.model.user.UserProfile

class UserValidator(
    override var value: UserProfile?
) : Validator<UserProfile, UserValidator>() {

    override fun _validate(value: UserProfile?): Boolean {
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

    override fun getValidate(): UserProfile = value!!
    override fun copy(value: UserProfile?): UserValidator {
        return UserValidator(value)
    }
}