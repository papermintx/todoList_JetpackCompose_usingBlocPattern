package com.example.belajarjetpackcompose.models
data class UserModel(
    var id: Int? = null,
    val name: String,
    val email: String,
    val numberPhone: Long,
    val isMarried: Boolean

)