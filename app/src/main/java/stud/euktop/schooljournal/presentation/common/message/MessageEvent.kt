package stud.euktop.schooljournal.presentation.common.message

import stud.euktop.schooljournal.presentation.common.message.contract.MessageParam

sealed interface MessageEvent {
    data class Message(val param: MessageParam) :
        stud.euktop.schooljournal.presentation.common.message.MessageEvent
}