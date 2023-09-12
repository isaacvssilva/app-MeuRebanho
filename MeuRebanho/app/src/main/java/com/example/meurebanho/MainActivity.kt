package com.example.meurebanho

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    /* lateinit -> atributo que ainda nao foi inicializado */
    lateinit var init_cadastro: TextView
    private lateinit var email: String
    private lateinit var senha: String

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    /* Criando instancia para o Firebase Authentication */
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /* Recebendo o identificador do TexView referente ao texto Criar conta */
        init_cadastro = findViewById(R.id.init_cadastro)

        /* Ao clicar no texto iniciar sessao, o usuario sera redirecionado para a tela de cadastro */
        val init_cadastro_intent = Intent(this, CadastroUsuarioActivity::class.java)
        init_cadastro.setOnClickListener {
            startActivity(init_cadastro_intent)
        }
        /* Deslogando o usuario atual do aplicativo */
        firebaseAuth.signOut()
        /* Ao preencher os campos de login corretamente, o usuario acessara o menu principal */
        clickEntrar()
    }
    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }

    /**
     * Funcao que verifica se o usuario ja esta logado. Se estiver logado,
     * direciona para o menu principal.
     */
    private fun verificarUsuarioLogado() {
        val usuarioAtual = firebaseAuth.currentUser
        if( usuarioAtual != null ){
            startActivity(
                Intent(this, MenuActivity::class.java)
            )
        }
    }

    /**
     * Funcao que configura um ouvinte de clique para o botao de entrada.
     * Quando clicar no botao, verifica se os campos estao preenchidos corretamente e,
     * se estiverem, inicia o processo de login.
     */
    private fun clickEntrar() {
        binding.bntInitLogin.setOnClickListener {
            if( validarCampos() ){
                logarUsuario()
            }
        }
    }

    /**
     * Funcao que realiza a autenticacao do usuario com as credenciais fornecidas (email e senha).
     * Exibe mensagens de sucesso ou erros e direciona para a o menu principal em caso de sucesso.
     */
    private fun logarUsuario() {

        firebaseAuth.signInWithEmailAndPassword(
            email, senha
        ).addOnSuccessListener {
            Toast.makeText(
                this,
                "Logado com sucesso!",
                Toast.LENGTH_LONG
            ).show()
            startActivity(
                Intent(this, MenuActivity::class.java)
            )
        }.addOnFailureListener { erro ->
            try {
                throw erro
            }catch ( erroUsuarioInvalido: FirebaseAuthInvalidUserException){
                erroUsuarioInvalido.printStackTrace()
                Toast.makeText(
                    this,
                    "E-mail não cadastrado!",
                    Toast.LENGTH_LONG
                ).show()
            }catch ( erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException){
                erroCredenciaisInvalidas.printStackTrace()
                Toast.makeText(
                    this,
                    "E-mail ou senha estão incorretos!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Funcao que Valida os campos de entrada do formulario de login.
     *
     * @return true se ambos os campos estao preenchidos corretamente, caso contrario, false.
     */
    private fun validarCampos(): Boolean {

        /* Obtendo os valores dos campos de login */
        email = binding.emailLoginUsuario.text.toString()
        senha = binding.senhaLoginUsuario.text.toString()

        /* Verificando se o campo "email" esta vazio */
        if (email.isEmpty()) {
            binding.emailLoginUsuario.error = "Preencha o seu e-mail."
            return false
        }

        /* Verificando se o campo "senha" esta vazio */
        if (senha.isEmpty()) {
            binding.senhaLoginUsuario.error = "Preencha a sua senha."
            return false
        }
        /* Todos os campos estao preenchidos corretamente */
        return true
    }
}