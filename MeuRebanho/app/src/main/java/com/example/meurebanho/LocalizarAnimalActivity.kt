package com.example.meurebanho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.meurebanho.databinding.ActivityLocalizarAnimalBinding

class LocalizarAnimalActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLocalizarAnimalBinding.inflate( layoutInflater )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )
        inicializaToolbar()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbLocalizaAnimal.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Localizar animal"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}