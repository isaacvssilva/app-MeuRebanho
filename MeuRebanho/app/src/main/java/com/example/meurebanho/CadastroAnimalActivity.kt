package com.example.meurebanho

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.set
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CadastroAnimalActivity : AppCompatActivity() {
    lateinit var datanasc_animal: EditText;
    private val calendar = Calendar.getInstance();
    val formatDAte=SimpleDateFormat("dd/MM/yyyy",Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_animal)

        datanasc_animal = findViewById<EditText>(R.id.cad_datanasc_animal)

        datanasc_animal.setOnClickListener {
            showDatePicker()
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
}