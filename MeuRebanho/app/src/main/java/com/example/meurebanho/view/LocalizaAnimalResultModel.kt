package com.example.meurebanho.view

data class LocalizaAnimalResultModel(
    var id: String,
    // imagemAnimal: String, // URL da imagem do animal (ou outra forma de representar a imagem)
    val raca: String, // Raça do animal
    var distancia: String // Distância do animal (em Km)
)
