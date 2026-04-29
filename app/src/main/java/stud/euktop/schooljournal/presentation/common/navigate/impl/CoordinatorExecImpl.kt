package stud.euktop.schooljournal.presentation.common.navigate.impl

import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.contract.CoordinatorExec
import stud.euktop.schooljournal.presentation.common.navigate.contract.ErrorHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoordinatorExecImpl @Inject constructor(
    private val errorHandler: ErrorHandler
) : CoordinatorExec {
    override suspend fun <T> exec(action: suspend () -> Result<T>): CoordinatorResult<T> {
        return try {
            action().fold(
                onSuccess = { CoordinatorResult.Success(it) },
                onFailure = { errorHandler.exec(it) }
            )
        } catch (e: Exception) {
            errorHandler.exec(e)
        }
    }
}