package com.example.meurebanho

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityRelatoriosBinding

class RelatoriosActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRelatoriosBinding.inflate( layoutInflater )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )
        inicializaToolbar()
    }
    private fun inicializaToolbar() {
        val toolbar = binding.tbRelatorios.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Relatórios"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}