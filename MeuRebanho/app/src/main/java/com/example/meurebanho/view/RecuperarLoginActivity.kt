package com.example.meurebanho.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.NetworkUtils
import com.example.meurebanho.databinding.ActivityRecuperarLoginBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperarLoginActivity : AppCompatActivity() {

    private lateinit var email_rec: String

    private val binding by lazy {
        ActivityRecuperarLoginBinding.inflate(layoutInflater)
    }

    /* Criando instancia para o Firebase Authentication */
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializaToolbar()
        clickRecuperar()
    }

    private fun clickRecuperar() {
        binding.btnRecuperarLogin.setOnClickListener {
            /* Verificando se há conexao com a internet */
            if (NetworkUtils.isInternetAvailable(this)) {
                if (validarCampos()) {
                    RecuperarLogin()
                }
            } else {
                Toast.makeText(
                    this,
                    "Sem conexão à Internet. Tente novamente mais tarde.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbRecLogin.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Recuperar Login"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun validarCampos(): Boolean {
        /* Obtendo os valores dos campos de login */
        email_rec = binding.RecEmailLoginUsuario.text.toString()

        /* Verificando se o campo "email" esta vazio */
        if (email_rec.isEmpty()) {
            binding.RecEmailLoginUsuario.error = "Preencha o seu e-mail."
            return false
        }
        /* Todos os campos estao preenchidos corretamente */
        return true
    }

    private fun RecuperarLogin() {
        firebaseAuth.sendPasswordResetEmail(email_rec).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Enviado link com sucesso", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_LONG).show()
            }

            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }
}