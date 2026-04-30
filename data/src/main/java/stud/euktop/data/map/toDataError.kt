package stud.euktop.data.map

import stud.euktop.domain.model.common.DataError
import stud.euktop.network.util.NetworkError

internal fun NetworkError.toDataError() = when (this) {
    is NetworkError.NetworkConnection -> DataError.NetworkConnection(message)
    is NetworkError.Unknown -> DataError.Unknown(message)
    is NetworkError.EmptyBody -> DataError.EmptyBody
    is NetworkError.HttpError -> DataError.HttpError(code, message)
}

internal fun Throwable.toDataError() =
    (this as? DataError) ?: (this as? NetworkError)?.toDataError() ?: DataError.Unknown(null)