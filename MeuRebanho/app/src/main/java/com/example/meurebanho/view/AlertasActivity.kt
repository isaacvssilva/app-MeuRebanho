package com.example.meurebanho.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.meurebanho.databinding.ActivityAlertasBinding
import com.google.android.material.appbar.AppBarLayout

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
        val toolbar: Toolbar = binding.tbAlertas.toolbar
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Alertas"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}