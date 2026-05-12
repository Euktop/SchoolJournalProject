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
    override fun copy(value: UserProfile?): UserValidator = UserValidator(value)

    // NEW: подробное сообщение о том, какое поле не прошло валидацию
    override fun getValidationErrorMessage(value: UserProfile?): String? {
        if (value == null) return "UserProfile is null"

        val failedFields = mutableListOf<String>()

        // проверяем каждое поле и собираем ошибки
        fun check(validator: Validator<*, *>, fieldName: String) {
            if (!validator.validate()) {
                // Получаем сообщение от внутреннего валидатора, если доступно
                val msg = (validator as? Validator<*, *>)?.let { v ->
                    try {
                        // Используем рефлексию или предполагаем, что у валидатора есть метод getValidationErrorMessage
                        // Упрощённо: вызываем validate(false) – но так мы залогируем повторно.
                        // Лучше добавить публичный метод для получения последней ошибки.
                        // Для простоты – ограничимся названием поля.
                        // В идеале каждый валидатор должен предоставлять сообщение, и мы его берём.
                        // Но т.к. validate() уже вызван ранее, мы можем создать временный экземпляр.
                        when (validator) {
                            is NameLetterOnlyValidator -> validator.getValidationErrorMessage(validator.value)
                            is EmailValidator -> validator.getValidationErrorMessage(validator.value)
                            is PhoneValidator -> validator.getValidationErrorMessage(validator.value)
                            else -> null
                        }
                    } catch (e: Exception) {
                        null
                    }
                }
                failedFields.add("$fieldName${if (msg != null) ": $msg" else ""}")
            }
        }

        check(NameLetterOnlyValidator(value.lastName), "lastName")
        check(NameLetterOnlyValidator(value.firstName), "firstName")
        check(NameLetterOnlyValidator(value.surName), "surName")
        check(EmailValidator(value.email), "email")
        check(PhoneValidator(value.phone), "phone")

        return if (failedFields.isNotEmpty()) {
            "UserProfile validation failed: ${failedFields.joinToString("; ")}"
        } else null
    }
}