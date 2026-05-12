package stud.euktop.domain.utils.validation

import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

abstract class Validator<T, V : Validator<T, V>> : ValidatorInterface<T> {

    override fun validate(value: T?): Boolean {
        val tag = this.toSimpleTag()
        val valueStr = value?.toSimpleTag().orEmpty()

        logger?.d(tag, "validation_start", "value=$valueStr")

        val result = try {
            _validate(value)
        } catch (e: Exception) {
            logger?.e(tag, "validation_exception", e, "value=$valueStr")
            false
        }

        if (result) {
            logger?.d(tag, "validation_success", "value=$valueStr")
        } else {
            val errorMessage = getValidationErrorMessage(value)
            val data = buildString {
                append("value=$valueStr")
                if (errorMessage != null) {
                    append(", reason=$errorMessage")
                }
            }
            logger?.e(tag, "validation_failed", data = data)
        }
        return result
    }

    /**
     * Переопределите для предоставления подробного сообщения об ошибке валидации.
     * @return сообщение, описывающее причину невалидности значения, или `null`
     */
    open fun getValidationErrorMessage(value: T?): String? = null

    companion object {
        fun isAllValidate(vararg values: Validator<*, *>?) =
            values.all { it?.validate() == true }

        fun isAllNullValidate(vararg values: Any?) = values.all { it != null }
    }

    protected abstract fun _validate(value: T?): Boolean

    abstract fun copy(value: T?): V
}