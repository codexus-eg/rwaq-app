package com.khater.rwaq.domain.model

 import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

sealed class SyncState {
    data object Offline : SyncState()

    @OptIn(ExperimentalUuidApi::class)
    data class DeletedChatsSynced(val chatIds: List<Uuid>) : SyncState()

    data class Error(val error: Throwable) : SyncState()
}