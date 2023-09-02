package com.example.meurebanho

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CadastroUsuarioActivity : AppCompatActivity() {
    /* lateinit -> atributo que ainda nao foi inicializado */
    lateinit var init_sessao: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        // ToDo
        // Ao clicar no texto "Iniciar sessao", o usuario sera redirecionado para a tela de
        // Login.

        /* Recebendo o identificador do TexView referente ao texto Iniciar sessao */
        init_sessao = findViewById(R.id.init_sessao)

        /* Ao clicar no texto iniciar sessao, o usuario sera redirecionado para o login */
        val init_login_intent = Intent(this, MainActivity::class.java)
        init_sessao.setOnClickListener {
            startActivity(init_login_intent)
        }
    }
}