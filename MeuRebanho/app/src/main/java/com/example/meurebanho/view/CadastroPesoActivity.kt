package com.example.meurebanho.view

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.meurebanho.NetworkUtils
import com.example.meurebanho.R
import com.example.meurebanho.controller.codes
import com.example.meurebanho.databinding.ActivityCadastroAnimalBinding
import com.example.meurebanho.databinding.ActivityCadastroPesoanimalBinding
import com.example.meurebanho.databinding.ActivityListPesosAnimaisBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CadastroPesoActivity : AppCompatActivity() {

    private lateinit var peso: EditText
    private lateinit var datapeso: EditText
    private lateinit var codigo:String
    private lateinit var document_id:String
    var peso_id="-1"
    private val calendar = Calendar.getInstance();
    val formatDAte = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private lateinit var binding: ActivityCadastroPesoanimalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroPesoanimalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        peso = findViewById<EditText>(R.id.peso_animal)
        datapeso = findViewById<EditText>(R.id.data_peso_animal)
        var btn_add_peso = findViewById<Button>(R.id.btn_peso_animal)

        initToolbal()

        if (intent.extras!!.getFloat(codes.Codes.CHAVE_PESO)!= null) {
            btn_add_peso.setText("Alterar")

            codigo=intent.extras!!.getString(codes.Codes.CHAVE_CODIGO).toString()
            document_id=intent.extras!!.getString(codes.Codes.CHAVE_DOCID).toString()
            peso_id=intent.extras!!.getString(codes.Codes.CHAVE_PESOID).toString()
            peso.setText(intent.extras!!.getFloat(codes.Codes.CHAVE_PESO).toString())
            datapeso.setText(intent.extras!!.getString(codes.Codes.CHAVE_DATAPESO))


        }else{
            codigo=intent.extras!!.getString(codes.Codes.CHAVE_CODIGO).toString()
            document_id=intent.extras!!.getString(codes.Codes.CHAVE_DOCID).toString()
        }

        btn_add_peso.setOnClickListener {
            clickAddPeso()

        }
        datapeso.setOnClickListener {
            showDatePicker()
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
                datapeso.setText(date)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show();
    }

    private fun validarCampos(): Boolean {

        /* Verificando se o campo "nome" esta vazio */
        if (peso.text.isEmpty()) {
            Toast.makeText(this, "Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        /* Verificando se o campo "email" esta vazio */
        if (datapeso.text.isEmpty()) {
            Toast.makeText(this, "Preencha o seu nome", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun initToolbal() {
        var toolbar =binding.tbCadpesoAni.toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle("Cadastro de peso")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater;
        inflater.inflate(R.menu.menu_consultar_animais, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun clickAddPeso() {
            /* Verificando se há conexao com a internet */
            if (NetworkUtils.isInternetAvailable(this)) {
                /* Verificando se todos os campos foram preenchidos */
                if (validarCampos()) {

                    var intent = Intent()

                    intent.putExtra(codes.Codes.CHAVE_CODIGO,codigo)
                    intent.putExtra(codes.Codes.CHAVE_PESO, (peso.text.toString()).toFloat())
                    intent.putExtra(codes.Codes.CHAVE_DATAPESO, datapeso.text.toString())
                    intent.putExtra(codes.Codes.CHAVE_DOCID, document_id)
                    intent.putExtra(codes.Codes.CHAVE_PESOID, peso_id)

                    setResult(codes.Codes.RESPONSE_OK, intent)
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
    }