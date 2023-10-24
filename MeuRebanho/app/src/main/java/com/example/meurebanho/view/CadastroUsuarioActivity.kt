package com.example.meurebanho.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.R
import com.example.meurebanho.databinding.ActivityCadastroUsuarioBinding
import com.example.meurebanho.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class CadastroUsuarioActivity : AppCompatActivity() {
    /* lateinit -> atributo que ainda nao foi inicializado */
    lateinit var init_sessao: TextView

    private val binding by lazy {
        ActivityCadastroUsuarioBinding.inflate(layoutInflater)
    }

    /* Variaveis para os campo de texto do cadastro */
    private lateinit var nome: String
    private lateinit var email: String
    private lateinit var cpf: String
    private  var telefone = 0
    private lateinit var senha: String

    /* Criando instancia para o Firebase Authentication */
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    /* Criando instancia para firestore DataBase */
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /* Recebendo o identificador do TexView referente ao texto Iniciar sessao */
        init_sessao = findViewById(R.id.init_sessao)

        /* Ao clicar no texto iniciar sessao, o usuario sera redirecionado para o login */
        val init_login_intent = Intent(this, MainActivity::class.java)
        init_sessao.setOnClickListener {
            startActivity(init_login_intent)
        }
        /* Ao preencher os campos de cadastro corretamente, o usuario sera cadastrado */
        clickCadastrar()
    }

    /**
     * Funcao que Valida os campos de entrada do formulario de cadastro.
     *
     * @return true se todos os campos estao sendo preenchidos corretamente, caso contrario, false.
     */
    private fun validarCampos(): Boolean {

        /* Obtendo os valores dos campos de cadastro */
        nome = binding.cadastroNomeUsuario.text.toString()
        email = binding.cadastroEmailUsuario.text.toString()
        cpf = binding.cadastroCpfUsuario.text.toString()
        telefone = binding.cadastroTelefoneUsuario.text.toString().toInt()
        senha = binding.cadastroSenhaUsuario.text.toString()

        /* Verificando se o campo "nome" esta vazio */
        if (nome.isEmpty()) {
            binding.cadastroNomeUsuario.error = "Preencha o seu nome."
            return false
        }
        /* Verificando se o campo "email" esta vazio */
        if (email.isEmpty()) {
            binding.cadastroEmailUsuario.error = "Preencha o seu e-mail."
            return false
        }
        /* Verificando se o campo "cpf" esta vazio */
        if (cpf.isEmpty()) {
            binding.cadastroCpfUsuario.error = "Preencha o seu CPF."
            return false
        }
        /* Verificando se o campo "telefone" esta vazio */
        if(telefone.equals(null)){
            binding.cadastroTelefoneUsuario.error = "Preencha o seu Telefone."
        }
        /* Verificando se o campo "senha" esta vazio */
        if (senha.isEmpty()) {
            binding.cadastroSenhaUsuario.error = "Preencha a sua senha."
            return false
        }
        /* Todos os campos estao preenchidos corretamente */
        return true
    }

    /**
     * Funcao que configura um ouvinte de clique para o botao de cadastrar.
     * Quando clicar no botao, verifica se os campos estao preenchidos corretamente e,
     * se estiverem, o cadastro do usuario foi realizado.
     */
    private fun clickCadastrar() {
        binding.bntCadastroUsuario.setOnClickListener {
            /* Verificando se todos os campos foram preenchidos */
            if( validarCampos() ){
                cadastrarUsuario(nome, email, cpf, telefone, senha)
            }
        }
    }

    /**
     * Cadastra um novo usuario usando Firebase Authentication e, em caso de sucesso,
     * salva os dados do usuario no Firestore.
     *
     * @param nome O nome do usuario.
     * @param email O endereco de e-mail do usuario.
     * @param cpf O numero de CPF do usuario.
     * @param telefone O numero de telefone do usuario.
     * @param senha A senha escolhida pelo usuario.
     */
    private fun cadastrarUsuario(nome: String, email: String, cpf: String, telefone: Int, senha: String) {

        firebaseAuth.createUserWithEmailAndPassword(
            email, senha
        ).addOnCompleteListener{ resultado ->
            if(resultado.isSuccessful){
                /* Salvando dados informados no Banco de dados */
                val idUsuario = resultado.result.user?.uid
                if(idUsuario != null){
                    val usr = User(
                        nome, email, cpf, telefone
                    )
                    salvarUsuarioFirestore(usr)
                }
            }
        }.addOnFailureListener{ erro ->
            try {
                throw erro
            }catch (erroSenhaFraca: FirebaseAuthWeakPasswordException) {
                erroSenhaFraca.printStackTrace()
                Toast.makeText(
                    applicationContext,
                    "Senha Fraca. Digite letras, números e caracteres especiais.",
                    Toast.LENGTH_LONG
                ).show()
            }catch (erroUsuarioExistente: FirebaseAuthUserCollisionException) {
                erroUsuarioExistente.printStackTrace()
                Toast.makeText(
                    applicationContext,
                    "E-mail já cadastrado.",
                    Toast.LENGTH_LONG
                ).show()
            }catch (erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException) {
                erroCredenciaisInvalidas.printStackTrace()
                Toast.makeText(
                    applicationContext,
                    "E-mail inválido. Digite outro e-mail",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Salva os dados do usuario no banco de dados Firestore.
     *
     * @param usr O objeto User contendo os dados do usuario a serem salvos.
     */
    private fun salvarUsuarioFirestore(usr: User) {
        firestore
            .collection("usuarios")
            .document( usr.cpfUser )
            .set( usr )
            .addOnSuccessListener {
                /* Em caso de sucesso, exibe uma mensagem de sucesso e direciona para a tela de login */
                Toast.makeText(applicationContext,
                    "Usuário cadastrado com sucesso!",
                    Toast.LENGTH_LONG).show()

                /* Enviando usuario para tela de login */
                startActivity(
                    Intent(applicationContext, MainActivity::class.java)
                )
            }.addOnFailureListener {
                /* Em caso de falha, exibe uma mensagem de erro */
                Toast.makeText(applicationContext,
                    "Erro ao fazer seu cadastro!",
                    Toast.LENGTH_LONG).show()
            }
    }
}
