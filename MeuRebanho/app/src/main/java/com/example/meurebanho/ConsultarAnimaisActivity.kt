package com.example.meurebanho

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityConsultarAnimaisBinding

class ConsultarAnimaisActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityConsultarAnimaisBinding.inflate( layoutInflater )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )
        inicializaToolbar()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbConsultaAnimal.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Consultar animais"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}