package stud.euktop.schooljournal.presentation.common.navigate.impl

import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.ErrorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.ErrorHandler
import javax.inject.Inject

class CoordinatorExecImpl @Inject constructor(
    private val errorHandler: ErrorHandler
) : CoordinatorExec {
    override suspend fun <T> exec(action: suspend () -> Result<T>): CoordinatorResult<T> {
        fun ErrorResult.toCoordinatorResult() = CoordinatorResult.Error(Throwable())
        return try {
            action().fold(
                onSuccess = { CoordinatorResult.Success(it) },
                onFailure = { errorHandler.exec(it).toCoordinatorResult() }
            )
        } catch (e: Exception) {
            errorHandler.exec(e).toCoordinatorResult()
        }
    }
}