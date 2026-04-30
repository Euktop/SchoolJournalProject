package stud.euktop.domain.utils.validation

import stud.euktop.domain.model.auth.Profile

class UserValidator(
    override var value: Profile?
) : Validator<Profile, UserValidator>() {

    override fun _validate(value: Profile?): Boolean {
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

    override fun getValidate(): Profile = value!!
    override fun copy(value: Profile?): UserValidator {
        return UserValidator(value)
    }
}