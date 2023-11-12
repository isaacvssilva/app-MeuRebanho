package com.example.meurebanho.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.NetworkUtils
import com.example.meurebanho.databinding.ActivityAtualizaTelefoneBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AtualizaTelefoneActivity : AppCompatActivity() {

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var novoTelefone: String
    private lateinit var binding: ActivityAtualizaTelefoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAtualizaTelefoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializaToolbar()
        clickAtualizaTelefone()
    }


    private fun inicializaToolbar() {
        val toolbar = binding.tbAtualizaTelefone.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Perfil"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun clickAtualizaTelefone() {
        /* Verificando se há conexao com a internet */
        binding.btnAtualizarTelefone.setOnClickListener {
            if (NetworkUtils.isInternetAvailable(this)) {
                if (validarCampos()) {
                    AtualizaTelefoneUsuario()
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

    private fun validarCampos(): Boolean {
        novoTelefone = binding.atualizaTelefoneUsuario.text.toString()

        /* Verificando se o campo "Telefone" esta vazio */
        if (novoTelefone.isEmpty()) {
            binding.atualizaTelefoneUsuario.error = "Preencha o seu telefone."
            return false
        }
        /* Todos os campos estao preenchidos corretamente */
        return true
    }

    private fun AtualizaTelefoneUsuario() {
        novoTelefone = binding.atualizaTelefoneUsuario.text.toString()
        val user = firebaseAuth.currentUser

        if (user != null) {
            // O usuário não é nulo, então podemos atualizar o Telefone no Firestore
            val userId = user.uid // Obtém o ID do usuário atual

            val userReference = bancoDados.collection("usuarios").document(userId)

            userReference.update("telefoneUser", novoTelefone)
                .addOnSuccessListener {
                    // A atualização do Telefone no Firestore foi bem-sucedida
                    Toast.makeText(this, "Telefone atualizado com sucesso", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
                .addOnFailureListener { e ->
                    // Falha na atualização do telefone no Firestore
                    Toast.makeText(
                        this,
                        "Falha ao atualizar o telefone no Firestore",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("AtualizaTelefoneActivity", "Erro no Firestore: $e")
                }
        } else {
            Toast.makeText(this, "Usuário nulo, faça login novamente", Toast.LENGTH_SHORT).show()
        }
    }
}