package com.example.meurebanho.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.R
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
        var btn_sair = findViewById<Button>(R.id.sair_app)
        btn_sair.setOnClickListener{
            deslogarUsuario()
        }

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

    private fun deslogarUsuario() {
        AlertDialog.Builder(this)
            .setTitle("Deslogar")
            .setMessage("Deseja realmente sair?")
            .setNegativeButton("Não") { dialog, posicao -> }
            .setPositiveButton("Sim") { dialog, posicao ->
                firebaseAuth.signOut()
                /* Encerrando todas as atividades e indo para a MainActivity */
                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .create()
            .show()
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