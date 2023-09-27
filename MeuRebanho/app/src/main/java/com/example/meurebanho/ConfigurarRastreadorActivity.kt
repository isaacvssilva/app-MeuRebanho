package com.example.meurebanho

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityConfigurarRastreadorBinding

class ConfigurarRastreadorActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityConfigurarRastreadorBinding.inflate( layoutInflater )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )
        inicializaToolbar()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbConfigRastreador.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Configurar rastreador"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}