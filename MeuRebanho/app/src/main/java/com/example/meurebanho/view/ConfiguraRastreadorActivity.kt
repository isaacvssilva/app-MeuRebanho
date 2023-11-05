package com.example.meurebanho.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityConfiguraRastreadorBinding

class ConfiguraRastreadorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfiguraRastreadorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfiguraRastreadorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializaToolbar()
    }


    private fun inicializaToolbar() {
        val toolbar = binding.tbConfigRastrador.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Rastreador"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}