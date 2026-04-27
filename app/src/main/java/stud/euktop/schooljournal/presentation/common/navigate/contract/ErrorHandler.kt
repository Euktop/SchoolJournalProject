package stud.euktop.schooljournal.presentation.common.navigate.contract

import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.ErrorResult

interface ErrorHandler {
    suspend fun exec(throwable: Throwable): ErrorResult
}