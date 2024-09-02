package com.example.belajarjetpackcompose.bloc.todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.belajarjetpackcompose.models.UserModel



class TodoViewModel : ViewModel(){
    private val _todos = MutableLiveData<State>(State.Empty)

    val todoState: LiveData<State> get() = _todos
    init {
        _todos.value = State.Success(emptyList())
    }

    fun onEvent(event: Event){
        when(event){
            is Event.AddData -> addData(event.user)
            is Event.RemoveData -> removeData(event.user)
            is Event.EditData -> editData(event.user)
            is Event.ClearData -> clearData()
        }
    }

    fun clearData(){
        _todos.value = State.Success(emptyList())
    }

    fun editData(user: UserModel){
        val currentState = _todos.value
        if(currentState is State.Success){
            val oldData = currentState.listData.find {
                it.id == user.id
            }

            if(oldData != null){
                val newData = currentState.listData.toMutableList().apply {
                    set(indexOf(oldData), user)
                }
                _todos.value = State.Success(newData)
            }else{
                _todos.value = State.Error("Data not found")
            }
        } else {
            _todos.value = State.Error("Invalid state")
        }
    }

    fun removeData(user: UserModel) {
        val currentState = _todos.value
        if (currentState is State.Success) {
            val oldData = currentState.listData.find {
                it.id == user.id
            }

            if (oldData != null) {
                // Menghapus user dari list jika oldData ditemukan
                val newData = currentState.listData.filter { it.id != user.id }
                _todos.value = State.Success(newData)
            } else {
                // Jika user tidak ditemukan di list
                _todos.value = State.Error("Data not found")
            }
        } else {
            _todos.value = State.Error("Invalid state")
        }
    }


    fun addData(user: UserModel) {
        val currentState = _todos.value
        if(currentState is State.Success){
            val length = currentState.listData.size
            user.id = length + 1
            val newData = currentState.listData + user

            _todos.value = State.Success(newData)
            return
        }
        _todos.value = State.Success(listOf(user))

    }

}