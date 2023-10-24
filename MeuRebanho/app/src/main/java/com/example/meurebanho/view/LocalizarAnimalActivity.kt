package com.example.meurebanho.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.meurebanho.R
import com.example.meurebanho.databinding.ActivityLocalizarAnimalBinding
import com.example.meurebanho.databinding.FragmentMainBinding

class LocalizarAnimalActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityLocalizarAnimalBinding.inflate( layoutInflater )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )
        inicializaToolbar()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbLocalizaAnimal.tbPrincipal
        setSupportActionBar( toolbar )
        supportActionBar?.apply {
            title = "Localizar animal"
            setDisplayHomeAsUpEnabled(true)
        }
    }

}