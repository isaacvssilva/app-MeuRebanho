package com.example.meurebanho

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {
    /* lateinit -> atributo que ainda nao foi inicializado */
    private lateinit var rvList: RecyclerView
    private val adapter = MenuItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        /* metodo inicial -> quando o android criar uma tela, este metodo vai ser chamado */
        iniciaView()
        setItemsLista()
    }

    private fun iniciaView() {
        rvList = findViewById(R.id.rv_list_menu)
        rvList.adapter = adapter

    }
    /* funcao responsavel por nomear os cards da lista do menu */
    private fun setItemsLista() {
        adapter.setItensList(
            arrayListOf(
                MenuItemModel(
                    "Cadastrar animal"
                ),
                MenuItemModel(
                    "Localizar animal"
                ),
                MenuItemModel(
                    "Consultar animais"
                ),
                MenuItemModel(
                    "Relatórios"
                )
            )
        )
    }
}