package com.example.meurebanho.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityAtualizaEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AtualizaEmailActivity : AppCompatActivity() {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
    }
    private lateinit var novoEmail: String

    private lateinit var binding: ActivityAtualizaEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAtualizaEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializaToolbar()
        clickAtualizaEmail()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbAtualizaEmail.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Perfil"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun clickAtualizaEmail() {
        binding.btnAtualizarEmail.setOnClickListener {
            if (validarCampos()) {
                AtualizaEmailUsuario()
            }
        }
    }

    private fun validarCampos(): Boolean {
        novoEmail = binding.atualizaEmailUsuario.text.toString()

        /* Verificando se o campo "email" esta vazio */
        if (novoEmail.isEmpty()) {
            binding.atualizaEmailUsuario.error = "Preencha o seu e-mail."
            return false
        }
        /* Todos os campos estao preenchidos corretamente */
        return true
    }

    private fun AtualizaEmailUsuario() {
        /* Obtendo o novo email fornecido pelo usuário a partir do campo de texto */
        novoEmail = binding.atualizaEmailUsuario.text.toString()

        /* Obtendo o usuário atualmente autenticado */
        val user = firebaseAuth.currentUser

        /* Verificando se o objeto user não é nulo */
        user?.updateEmail(novoEmail)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val userId = user.uid

                    /* Obtendo uma referência ao documento do usuário no Firestore */
                    val userReference = bancoDados.collection("usuarios").document(userId)

                    /* Atualizando o campo "emailUser" com o novo email */
                    userReference.update("emailUser", novoEmail)
                        .addOnSuccessListener {
                            // A atualização do email no Firestore foi bem-sucedida */
                            Toast.makeText(this, "Email atualizado com sucesso", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            // Falha na atualização do email no Firestore */
                            Toast.makeText(
                                this,
                                "Falha ao atualizar o email no Firestore",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("AtualizaEmailActivity", "Erro no Firestore: $e")
                        }
                } else {
                    /* Falha na atualização do email no Firebase Authentication */
                    Toast.makeText(
                        this,
                        "Falha ao atualizar o email no Firebase Authentication",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(
                        "AtualizaEmailActivity",
                        "Erro no Firebase Authentication: ${task.exception?.message}"
                    )
                }
            }
    }

}