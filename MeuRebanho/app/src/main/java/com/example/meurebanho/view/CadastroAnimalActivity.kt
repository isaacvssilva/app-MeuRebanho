package com.example.meurebanho.view

import android.R.id
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.NetworkUtils
import com.example.meurebanho.R
import com.example.meurebanho.controller.codes.Codes
import com.example.meurebanho.databinding.ActivityCadastroAnimalBinding
import com.example.meurebanho.model.Animal
import com.example.meurebanho.rebanhoDAO.rebanhoDAOinterface
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldPath.documentId
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CadastroAnimalActivity : AppCompatActivity() {
    private val calendar = Calendar.getInstance();

    /* Variaveis para os campo de texto do cadastro */
    private lateinit var especie: EditText
    private var sexo: String ="M"
    private lateinit var cor: EditText
    private var codigo_doc= "-1"
    private lateinit var raca: EditText
    private lateinit var codigo: EditText
    private lateinit var datanasc: EditText
    private lateinit var button_add: Button
    private lateinit var binding: ActivityCadastroAnimalBinding


    val formatDAte = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroAnimalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        datanasc = findViewById<EditText>(R.id.cad_datanasc_animal)
        especie = findViewById<EditText>(R.id.cad_especie_animal)
        raca = findViewById<EditText>(R.id.cad_raca_animal)
        cor = findViewById<EditText>(R.id.cad_cor_animal)
        codigo = findViewById<EditText>(R.id.cad_codigo_animal)
        button_add = findViewById<Button>(R.id.btncad_animal)
        var radiosexo = findViewById<RadioGroup>(R.id.groupbutton)

        inicializaToolbar()

        if (intent.extras != null) {
            button_add.setText("Alterar")

            datanasc.setText(intent.extras!!.getString(Codes.CHAVE_DATANASC))
            especie.setText(intent.extras!!.getString(Codes.CHAVE_ESPECIE))
            raca.setText (intent.extras!!.getString(Codes.CHAVE_RACA))
            cor.setText (intent.extras!!.getString(Codes.CHAVE_COR))
            codigo_doc= intent.extras!!.getString(Codes.CHAVE_DOCID).toString()
            codigo.setText(intent.extras!!.getString(Codes.CHAVE_CODIGO))
            radiosexo.check(R.id.sexo_femi)

        }

        radiosexo.setOnCheckedChangeListener { group, isChecked ->
            sexo=group.findViewById<RadioButton>(isChecked).text.toString()
            Toast.makeText(this, "sexo:"+sexo, Toast.LENGTH_SHORT).show()
        }
        datanasc.setOnClickListener {
            showDatePicker()
        }
        button_add.setOnClickListener {
            clickCadastrar()
        }
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            android.R.style.Theme_Holo_Dialog_MinWidth,
            DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, i)
                selectedDate.set(Calendar.MONTH, i2)
                selectedDate.set(Calendar.DAY_OF_MONTH, i3)
                val date: String = formatDAte.format(selectedDate.time)
                Toast.makeText(this, "Date:" + date, Toast.LENGTH_SHORT).show()
                datanasc.setText(date)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show();
    }

    /**
     * Funcao que Valida os campos de entrada do formulario de cadastro.
     *
     * @return true se todos os campos estao sendo preenchidos corretamente, caso contrario, false.
     */
    private fun validarCampos(): Boolean {

        /* Verificando se o campo "nome" esta vazio */
        if (especie.text.isEmpty()) {
            Toast.makeText(this, "Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        /* Verificando se o campo "email" esta vazio */
        if (raca.text.isEmpty()) {
            Toast.makeText(this, "Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        /* Verificando se o campo "cpf" esta vazio */
        if (cor.text.isEmpty()) {
            Toast.makeText(this, "Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        /* Verificando se o campo "telefone" esta vazio */
        if (codigo.text.isEmpty()) {
            Toast.makeText(this, "Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        /* Verificando se o campo "senha" esta vazio */
        if (datanasc.text.isEmpty()) {
            Toast.makeText(this, "Preencha o seu nome", Toast.LENGTH_SHORT).show()
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
        /* Verificando se há conexao com a internet */
        if (NetworkUtils.isInternetAvailable(this)) {
            /* Verificando se todos os campos foram preenchidos */
            if (validarCampos()) {

                var intent = Intent()

                intent.putExtra(Codes.CHAVE_CODIGO,codigo.text.toString())
                intent.putExtra(Codes.CHAVE_ESPECIE, especie.text.toString())
                intent.putExtra(Codes.CHAVE_RACA, raca.text.toString())
                intent.putExtra(Codes.CHAVE_COR,cor.text.toString())
                intent.putExtra(Codes.CHAVE_DATANASC,datanasc.text.toString())
                intent.putExtra(Codes.CHAVE_SEXO,sexo)
                intent.putExtra(Codes.CHAVE_DOCID,codigo_doc)

               // animalDAO.addAnimal(animal);
                //salvarUsuarioFirestore(animal)
                /* criando nó no Firebase Realtime Database */
                //nodeFilhoAnimalRTDB(codigo.text.toString())

                Toast.makeText(this, "Animal cadastrado com sucesso", Toast.LENGTH_SHORT).show()

                setResult(Codes.RESPONSE_OK, intent)
                finish()
            }
        } else {
            Toast.makeText(
                this,
                "Sem conexão à Internet. Tente novamente mais tarde.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbCadastroAni.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            if (intent.extras != null)
                title = "Alterar Animal"
            else
                title = "Cadastrar Animal"
            setDisplayHomeAsUpEnabled(true)
        }
    }


    /**
     * Funcao que cria um novo nó filho no Real-Time Database com o identificador
     * de cadastro do animal.
     *
     * Este no filho é associado ao no "Animal" no Firebase Realtime Database.
     *
     * @param animalId O identificador de cadastro do animal que sera usado como parte do caminho no no.
     */
//    private fun nodeFilhoAnimalRTDB(animalId: String) {
//        /* Obtendo uma referencia ao no "Animal" no Firebase Realtime Database */
//        val database = FirebaseDatabase.getInstance()
//        val animalRef = database.getReference("Animal/$animalId/")
//
//        /* Criando um novo no com o ID do animal como chave */
//        val novoAnimalRef = animalRef.child("GPS")
//
//        /* Adicionando os dados ao novo nó */
//        novoAnimalRef.setValue("")
//    }
    }

