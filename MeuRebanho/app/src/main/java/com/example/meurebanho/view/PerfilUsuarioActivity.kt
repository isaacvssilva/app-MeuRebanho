package com.example.meurebanho.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityPerfilUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PerfilUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilUsuarioBinding

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inicializaToolbar()
        dadosUsuario()
        editarNome()
        editarTelefone()
        editarEmail()
        editarSenha()
        deletarConta()
    }

    private fun deletarConta() {
        binding.deletarUsuario.setOnClickListener {
            Alerta()
        }
    }

    private fun Alerta() {
        AlertDialog.Builder(this)
            .setTitle("Deletar perfil")
            .setMessage("Deseja realmente excluir a sua conta?")
            .setNegativeButton("Não") { dialog, posicao -> }
            .setPositiveButton("Sim") { dialog, posicao ->
                deletarUsuario()
            }
            .create()
            .show()

    }

    private fun deletarUsuario() {
        Toast.makeText(this, "verificação", Toast.LENGTH_SHORT).show()
        startActivity(
            Intent(this, DeletaUsuarioActivity::class.java)
        )
    }

    private fun editarNome() {
        binding.editarNomeUsuario.setOnClickListener {
            startActivity(
                Intent(this, AtualizaNomeActivity::class.java)
            )
        }
    }

    private fun editarTelefone() {
        binding.editarTelefoneUsuario.setOnClickListener {
            startActivity(
                Intent(this, AtualizaTelefoneActivity::class.java)
            )
        }
    }

    private fun editarEmail() {
        binding.editarEmailUsuario.setOnClickListener {
            startActivity(
                Intent(this, AtualizaEmailActivity::class.java)
            )
        }
    }

    private fun editarSenha() {
        binding.editarSenhaUsuario.setOnClickListener {
            startActivity(
                Intent(this, AtualizaSenhaActivity::class.java)
            )
        }
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbPerfil.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Perfil"
            setDisplayHomeAsUpEnabled(true)
        }
    }


    private fun dadosUsuario() {
        val idUsuario = firebaseAuth.currentUser?.uid
        if (idUsuario != null) {

            val refUsuario = bancoDados.collection("usuarios").document(idUsuario)

            refUsuario.addSnapshotListener { valor, erro ->
                val dados = valor?.data
                if (dados != null) {
                    val nome = dados["nomeUser"]
                    val telefone = dados["telefoneUser"]
                    val email = dados["emailUser"]
                    binding.nomePerfil.setText(nome.toString())
                    binding.telefonePerfil.setText(telefone.toString())
                    binding.emailPerfil.setText(email.toString())
                }
            }
        }
    }
}