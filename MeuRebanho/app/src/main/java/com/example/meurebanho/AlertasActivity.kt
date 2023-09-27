package com.example.meurebanho

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityAlertasBinding

class AlertasActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityAlertasBinding.inflate( layoutInflater )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )
        inicializaToolbar()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbAlertas.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Alertas"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}