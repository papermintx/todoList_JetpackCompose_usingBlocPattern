package com.example.belajarjetpackcompose.bloc.todo.viewmodel

import com.example.belajarjetpackcompose.models.UserModel

sealed class Event {
    data class AddData(
        val user: UserModel
    ) : Event()
    data class RemoveData(
        val user: UserModel
    ) : Event()

    data class EditData(
        val user: UserModel
    ) : Event()

    object ClearData : Event()
}