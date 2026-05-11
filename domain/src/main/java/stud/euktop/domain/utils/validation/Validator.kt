package stud.euktop.domain.utils.validation

import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

abstract class Validator<T, V : Validator<T, V>> : ValidatorInterface<T> {

    /**
     * Валидирует значение.
     * @param value Значение для проверки
     * @return Результат валидации
     */
    override fun validate(value: T?): Boolean {
        val result = _validate(value)
        if (!result)
            logger?.e(this.toSimpleTag(), "value:${value?.toSimpleTag()}")
        return result
    }

    companion object {
        fun isAllValidate(vararg values: Validator<*, *>?) =
            values.all { it?.validate() == true }

        fun isAllNullValidate(vararg values: Any?) = values.all { it != null }
    }

    protected abstract fun _validate(value: T? = this.value): Boolean
    abstract fun copy(value: T?): V
}