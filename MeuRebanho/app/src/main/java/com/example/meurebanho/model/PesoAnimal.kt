package com.example.meurebanho.model

import com.google.firebase.Timestamp
import com.google.type.Date

class PesoAnimal(
    var codigo_animal: String,
    var peso:Float,
    var data_peso: java.util.Date,
    var document_id_animal:String,
    var document_id_peso:String
)