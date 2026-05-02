package stud.euktop.schooljournal.presentation.common.navigate

interface RepositoryExec {
    var onError: (CoordinatorResult.Error) -> Unit
}