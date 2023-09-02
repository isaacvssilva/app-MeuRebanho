package com.example.meurebanho

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    /* lateinit -> atributo que ainda nao foi inicializado */
    lateinit var iniciaLogin: Button
    lateinit var init_cadastro: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ToDo
        // Ao clicar no botao Entrar, sera feito a verificacao das credenciais e,
        // apos confirmar, sera redirecionado ao menu principal.


        /* Recebendo o identificador do botao referente a confirmacao de login do usuario */
        iniciaLogin = findViewById(R.id.bnt_init_login)

        /* Ao clicar no botao Entrar,  o menu principal eh exibido */
        val menu_intent = Intent(this, MenuActivity::class.java)
        iniciaLogin.setOnClickListener {
            startActivity(menu_intent)
        }

        /*----------------------------------------------------------------------------------------*/
        // ToDo
        // Ao clicar no texto "Criar conta", o usuario sera redirecionado para a tela de
        // Cadastro.

        /* Recebendo o identificador do TexView referente ao texto Criar conta */
        init_cadastro = findViewById(R.id.init_cadastro)

        /* Ao clicar no texto iniciar sessao, o usuario sera redirecionado para a tela de cadastro */
        val init_cadastro_intent = Intent(this, CadastroUsuarioActivity::class.java)
        init_cadastro.setOnClickListener {
            startActivity(init_cadastro_intent)
        }
    }
}