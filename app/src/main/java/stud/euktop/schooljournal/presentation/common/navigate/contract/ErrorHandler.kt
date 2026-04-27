package stud.euktop.schooljournal.presentation.common.navigate.contract

import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult

interface ErrorHandler {
    suspend fun exec(throwable: Throwable): CoordinatorResult.Error
}