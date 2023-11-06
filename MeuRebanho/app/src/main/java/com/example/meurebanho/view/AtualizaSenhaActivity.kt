package com.example.meurebanho.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.NetworkUtils
import com.example.meurebanho.databinding.ActivityAtualizaSenhaBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class AtualizaSenhaActivity : AppCompatActivity() {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private lateinit var senhaAtual: String
    private lateinit var novaSenha: String

    private lateinit var binding: ActivityAtualizaSenhaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAtualizaSenhaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializaToolbar()
        clickAtualizaSenha()

    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbAtualizaSenha.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Perfil"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun clickAtualizaSenha() {
        binding.btnAtualizarSenha.setOnClickListener {
            /* Verificando se há conexao com a internet */
            if (NetworkUtils.isInternetAvailable(this)) {
                if (validarCampos()) {
                    AtualizaSenhaUsuario()
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
        senhaAtual = binding.senhaAtualUsuario.text.toString()
        novaSenha = binding.atualizaSenhaUsuario.text.toString()

        /* Verificando se o campo "senha atual" esta vazio */
        if (senhaAtual.isEmpty()) {
            binding.senhaAtualUsuario.error = "Preencha com a senha atual."
            return false
        }

        /* Verificando se o campo "nova senha" esta vazio */
        if (novaSenha.isEmpty()) {
            binding.atualizaSenhaUsuario.error = "Preencha com a nova senha."
            return false
        }

        /* Todos os campos estao preenchidos corretamente */
        return true
    }

    private fun AtualizaSenhaUsuario() {
        senhaAtual = binding.senhaAtualUsuario.text.toString()
        novaSenha = binding.atualizaSenhaUsuario.text.toString()

        // Obtenha a instância do usuário atual
        val user = firebaseAuth.currentUser

        // Verifique se o usuário está logado
        if (user != null) {
            // Reautentique o usuário para verificar a senha atual
            val credential = EmailAuthProvider.getCredential(user.email!!, senhaAtual)
            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        // A reautenticação foi bem-sucedida, agora atualize a senha
                        user.updatePassword(novaSenha)
                            .addOnCompleteListener { passwordUpdateTask ->
                                if (passwordUpdateTask.isSuccessful) {
                                    // A senha foi atualizada com sucesso
                                    Toast.makeText(
                                        this,
                                        "Senha atualizada com sucesso",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    // Falha na atualização da senha
                                    Toast.makeText(
                                        this,
                                        "Falha ao atualizar a senha",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e(
                                        "AtualizaSenhaActivity",
                                        "Erro na atualização de senha: ${passwordUpdateTask.exception?.message}"
                                    )
                                }
                            }
                    } else {
                        // Falha na reautenticação (senha atual incorreta)
                        Toast.makeText(this, "Senha atual incorreta", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            // O usuário não está logado
            Toast.makeText(this, "Usuário não está logado", Toast.LENGTH_SHORT).show()
        }
    }


}