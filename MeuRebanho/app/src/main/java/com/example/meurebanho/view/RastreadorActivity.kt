package com.example.meurebanho.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityRastreadorBinding

class RastreadorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRastreadorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRastreadorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializaToolbar()
        configuraRastreador()
        configuraGateway()
    }

    private fun configuraRastreador() {
        binding.configRastreador.setOnClickListener {
            startActivity(
                Intent(this, ConfiguraRastreadorActivity::class.java)
            )
        }
    }

    private fun configuraGateway() {
        binding.configGateway.setOnClickListener {
            AlertaConfigGateway()
        }
    }

    private fun AlertaConfigGateway() {
        AlertDialog.Builder(this)
            .setTitle("Alerta!")
            .setMessage("Para continuar, verifique sua conexão com a rede do Gateway!")
            .setNegativeButton("Não estou conectado") { dialog, posicao -> }
            .setPositiveButton("Estou conectado") { dialog, posicao ->
                val intent = Intent(this, ConfiguraGatewayActivity::class.java)
                startActivity(intent)
            }
            .create()
            .show()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbRastreador.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Configurações"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}



