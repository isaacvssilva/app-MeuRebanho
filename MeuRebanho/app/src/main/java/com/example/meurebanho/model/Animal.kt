package com.example.meurebanho.model

import java.io.Serializable

data class Animal (
    var raca: String,
    var especie:String,
    var cor: String,
    var sexo: String,
    var datanasc: String,
    var codigo: String,
    var documentID:String="0",
    var imageResId:Int):Serializable

