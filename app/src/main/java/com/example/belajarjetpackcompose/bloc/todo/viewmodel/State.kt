package com.example.belajarjetpackcompose.bloc.todo.viewmodel
import com.example.belajarjetpackcompose.models.UserModel
sealed class State {
    object Loading : State()

    data class Success(
        val listData: List<UserModel>
    ) : State()

    data class Error(
        val message: String
    ) : State()

    object Empty : State()
}