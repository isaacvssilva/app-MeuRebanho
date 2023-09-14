package com.example.meurebanho

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import com.example.meurebanho.model.Animal
import com.example.meurebanho.model.User
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CadastroAnimalActivity : AppCompatActivity() {
    private val calendar = Calendar.getInstance();

    /* Variaveis para os campo de texto do cadastro */
    private lateinit var especie: EditText
    private lateinit var sexo: EditText
    private lateinit var  cor: EditText
    private lateinit var raca: EditText
    private lateinit var codigo: EditText
    private lateinit var datanasc_animal: EditText
    private lateinit var button_add: Button


    val formatDAte=SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_animal)

        datanasc_animal = findViewById<EditText>(R.id.cad_datanasc_animal)
        especie = findViewById<EditText>(R.id.cad_especie_animal)
        raca = findViewById<EditText>(R.id.cad_raca_animal)
        cor = findViewById<EditText>(R.id.cad_cor_animal)
        sexo = findViewById<EditText>(R.id.cad_sexo_animal)
        codigo = findViewById<EditText>(R.id.cad_codigo_animal)
        button_add = findViewById<Button>(R.id.btncad_animal)

        datanasc_animal.setOnClickListener {
            showDatePicker()
        }
        button_add.setOnClickListener{
            clickCadastrar()
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            android.R.style.Theme_Holo_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,i)
                selectedDate.set(Calendar.MONTH,i2)
                selectedDate.set(Calendar.DAY_OF_MONTH,i3)
                val date:String =formatDAte.format(selectedDate.time)
                Toast.makeText(this,"Date:"+ date, Toast.LENGTH_SHORT).show()
                datanasc_animal.setText(date)

            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH))
            datePicker.show();
    }


    /* Criando instancia para firestore DataBase */
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    /**
     * Funcao que Valida os campos de entrada do formulario de cadastro.
     *
     * @return true se todos os campos estao sendo preenchidos corretamente, caso contrario, false.
     */
    private fun validarCampos(): Boolean {

        /* Verificando se o campo "nome" esta vazio */
        if (especie.getText().isEmpty()) {
            Toast.makeText(this,"Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        /* Verificando se o campo "email" esta vazio */
        if (raca.getText().isEmpty()) {
            Toast.makeText(this,"Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        /* Verificando se o campo "cpf" esta vazio */
        if (cor.getText().isEmpty()) {
            Toast.makeText(this,"Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        /* Verificando se o campo "telefone" esta vazio */
        if(codigo.getText().isEmpty()){
            Toast.makeText(this,"Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        /* Verificando se o campo "senha" esta vazio */
        if (datanasc_animal.getText().isEmpty()) {
            Toast.makeText(this,"Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        if (sexo.getText().isEmpty()) {
            Toast.makeText(this,"Preencha o seu nome", Toast.LENGTH_SHORT).show()
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
            /* Verificando se todos os campos foram preenchidos */
            if( validarCampos() ){
                val animal= Animal(
                    especie.getText().toString(),
                    raca.getText().toString(),
                    cor.getText().toString(),
                    sexo.getText().toString(),
                    datanasc_animal.getText().toString(),
                    codigo.getText().toString())
                salvarUsuarioFirestore(animal)
            }
    }


    /**
     * Salva os dados do usuario no banco de dados Firestore.
     *
     * @param usr O objeto User contendo os dados do usuario a serem salvos.
     */
    private fun salvarUsuarioFirestore(animal: Animal) {
        firestore
            .collection("Animais")
            .document( animal.codigo )
            .set( animal )
            .addOnSuccessListener {
                /* Em caso de sucesso, exibe uma mensagem de sucesso e direciona para a tela de login */
                Toast.makeText(applicationContext,
                    "animal cadastrado com sucesso!",
                    Toast.LENGTH_LONG).show()

            }.addOnFailureListener {
                /* Em caso de falha, exibe uma mensagem de erro */
                Toast.makeText(applicationContext,
                    "Erro ao fazer seu cadastro!",
                    Toast.LENGTH_LONG).show()
            }
    }
}
