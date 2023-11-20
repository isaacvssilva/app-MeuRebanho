package com.example.meurebanho.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.example.meurebanho.R

class PesoAnimal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peso_animal)
        var peso_animal = findViewById<EditText>(R.id.peso_animal)
        var datanasc_animal = findViewById<EditText>(R.id.data_peso_animal)
        var btn_add_peso = findViewById<Button>(R.id.btn_peso_animal)
        initToolbal()
        btn_add_peso.setOnClickListener {
            clickAddPeso()

        }


    }

    private fun initToolbal() {
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle("Cadastro de peso")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater;
        inflater.inflate(R.menu.menu_consultar_animais, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun clickAddPeso() {}
}