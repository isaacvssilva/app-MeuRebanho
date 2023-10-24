package com.example.meurebanho.model

import java.io.Serializable

data class Animal (
    var especie: String,
    var raca: String,
    var cor: String,
    var sexo: String,
    var datanasc: String,
    var codigo: String,
    var imageResId:Int):Serializable

