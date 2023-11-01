package com.example.meurebanho.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityDeletaUsuarioBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DeletaUsuarioActivity : AppCompatActivity() {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var senhaDeletaConta: String
    private lateinit var binding: ActivityDeletaUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeletaUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickConfirmar()
    }

    private fun clickConfirmar() {
        binding.btnConfirmaDelete.setOnClickListener {
            if (validarCampos()) {
                verificacaoSegura()
            }
        }
    }

    private fun validarCampos(): Boolean {
        senhaDeletaConta = binding.senhaDeletaUsuario.text.toString()
        /* Verificando se o campo "senha" esta vazio */
        if (senhaDeletaConta.isEmpty()) {
            binding.senhaDeletaUsuario.error = "Preencha com sua senha."
            return false
        }
        /* Todos os campos estao preenchidos corretamente */
        return true
    }

    private fun verificacaoSegura() {
        /* Obtendo a senha fornecida pelo usuário a partir do campo de texto */
        senhaDeletaConta = binding.senhaDeletaUsuario.text.toString()

        /* Obtendo o usuário atualmente autenticado */
        val user = firebaseAuth.currentUser

        /* Verificando se o objeto user não é nulo */
        if (user != null) {
            /* Criando um objeto de credencial para autenticação do usuário */
            val credential = EmailAuthProvider.getCredential(user.email ?: "", senhaDeletaConta)

            /* Reautenticando o usuário com a credencial */
            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        /* O usuário foi autenticado novamente com sucesso */
                        /* ToDo: Apagando os dados do usuário no Firestore */

                        val userId = user.uid
                        if (userId != null) {
                            /* Acessando a coleção de "usuarios" e excluindo o documento com o ID do usuário */
                            bancoDados.collection("usuarios")
                                .document(userId)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        this,
                                        "Dados da conta no Firestore apagados com sucesso",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }

                        /* Excluindo a conta do usuário no Firebase Authentication */
                        user.delete()
                            .addOnCompleteListener { deleteTask ->
                                if (deleteTask.isSuccessful) {
                                    /* Conta do usuário excluída com sucesso */
                                    Toast.makeText(
                                        this,
                                        "Conta deletada com sucesso",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    /* Encerrando todas as atividades e indo para a MainActivity */
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                } else {
                                    /* Falha ao excluir a conta do usuário */
                                    Toast.makeText(
                                        this,
                                        "Falha ao deletar a conta",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        /* Falha na reautenticação do usuário */
                        Toast.makeText(this, "Falha na autenticação", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
        }
    }
}