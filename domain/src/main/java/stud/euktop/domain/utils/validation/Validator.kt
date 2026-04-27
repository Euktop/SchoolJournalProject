package stud.euktop.domain.utils.validation

import stud.euktop.domain.utils.loger.logger
import stud.euktop.domain.utils.loger.toSimpleTag

abstract class Validator<T, V : Validator<T, V>> {
    abstract var value: T?

    /**
     * Валидирует значение.
     * @param value Значение для проверки
     * @return Результат валидации
     */
    fun validate(value: T? = this.value): Boolean {
        val result = _validate(value)
        if (!result)
            logger?.e(this.toSimpleTag(), "value:${value?.toSimpleTag()}")
        return result
    }

    companion object {
        fun isAllValidate(vararg values: Validator<*, *>) =
            values.all { it.validate() }

        fun isAllNullValidate(vararg values: Any?) = values.all { it != null }
    }

    protected abstract fun _validate(value: T? = this.value): Boolean
    abstract fun getValidate(): T
    abstract fun copy(value: T?): V
}