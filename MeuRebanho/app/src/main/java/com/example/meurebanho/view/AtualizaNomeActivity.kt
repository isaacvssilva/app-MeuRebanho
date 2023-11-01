package com.example.meurebanho.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityAtualizaNomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AtualizaNomeActivity : AppCompatActivity() {

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var novoNome: String

    private lateinit var binding: ActivityAtualizaNomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAtualizaNomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializaToolbar()
        clickAtualizaNome()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbAtualizaNome.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Perfil"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun clickAtualizaNome() {
        binding.btnAtualizarNome.setOnClickListener {
            if (validarCampos()) {
                AtualizaNomeUsuario()
            }
        }
    }

    private fun validarCampos(): Boolean {
        novoNome = binding.atualizaNomeUsuario.text.toString()

        /* Verificando se o campo "nome" esta vazio */
        if (novoNome.isEmpty()) {
            binding.atualizaNomeUsuario.error = "Preencha o seu nome."
            return false
        }
        /* Todos os campos estao preenchidos corretamente */
        return true
    }

    private fun AtualizaNomeUsuario() {
        novoNome = binding.atualizaNomeUsuario.text.toString()
        val user = firebaseAuth.currentUser

        if (user != null) {
            // O usuário não é nulo, então podemos atualizar o nome no Firestore
            val userId = user.uid // Obtém o ID do usuário atual

            val userReference = bancoDados.collection("usuarios").document(userId)

            userReference.update("nomeUser", novoNome)
                .addOnSuccessListener {
                    // A atualização do nome no Firestore foi bem-sucedida
                    Toast.makeText(this, "Nome atualizado com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    // Falha na atualização do nome no Firestore
                    Toast.makeText(this, "Falha ao atualizar o nome no Firestore", Toast.LENGTH_SHORT).show()
                    Log.e("AtualizaNomeActivity", "Erro no Firestore: $e")
                }
        } else {
            // O usuário é nulo, lide com isso adequadamente (por exemplo, solicite que o usuário faça login)
            Toast.makeText(this, "Usuário nulo, faça login novamente", Toast.LENGTH_SHORT).show()
            // Adicione a lógica de redirecionamento para a tela de login ou trate apropriadamente
        }
    }

}