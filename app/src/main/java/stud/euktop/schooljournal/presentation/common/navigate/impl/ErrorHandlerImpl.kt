package stud.euktop.schooljournal.presentation.common.navigate.impl

import stud.euktop.domain.model.common.DataError
import stud.euktop.schooljournal.R
import stud.euktop.schooljournal.presentation.common.navigate.CoordinatorResult
import stud.euktop.schooljournal.presentation.common.navigate.NavCommand
import stud.euktop.schooljournal.presentation.common.navigate.contract.ErrorHandler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandlerImpl @Inject constructor() : ErrorHandler {
    override suspend fun exec(throwable: Throwable): CoordinatorResult.Error {
        val (msgId, navCmd) = when (throwable) {
            is DataError.NetworkConnection ->
                R.string.error_network to NavCommand.Back

            is DataError.HttpError -> {
                val id = when (throwable.code) {
                    401 -> R.string.error_auth_expired
                    403 -> R.string.error_no_permission
                    404 -> R.string.error_not_found
                    in 500..599 -> R.string.error_server
                    else -> R.string.error_unknown
                }
                id to NavCommand.Back
            }

            is DataError.EmptyBody ->
                R.string.error_empty_body to NavCommand.Back

            else ->
                R.string.error_unknown to NavCommand.Back
        }
        return CoordinatorResult.Error(navCmd, msgId)
    }
}