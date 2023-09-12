package com.example.meurebanho.model

data class User(
    val nomeUser: String,
    val emailUser: String,
    val cpfUser: String,
    val telefoneUser: Int = 0
)
