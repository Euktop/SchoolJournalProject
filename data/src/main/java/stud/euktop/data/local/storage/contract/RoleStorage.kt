package stud.euktop.data.local.storage.contract

import stud.euktop.domain.model.user.Role

interface RoleStorage : BaseStorage {
    suspend fun getRole(): Role?
    suspend fun saveRole(role: Role?)
}