package stud.euktop.schooljournal.presentation.common.navigate.contract

import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult

interface CoordinatorExec {
    suspend fun <T> exec(action: suspend () -> Result<T>): CoordinatorResult<T>
}