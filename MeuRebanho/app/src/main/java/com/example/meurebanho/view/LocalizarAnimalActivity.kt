package com.example.meurebanho.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.meurebanho.R
import com.example.meurebanho.databinding.ActivityLocalizarAnimalBinding
import com.google.firebase.firestore.FirebaseFirestore

class LocalizarAnimalActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLocalizarAnimalBinding.inflate(layoutInflater)
    }

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var recycleViewLocAnimal: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var locAnimalAdapter: LocalizaAnimalResultAdapter
    private lateinit var locAnimalList: ArrayList<LocalizaAnimalResultModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        inicializaToolbar()
        initializeRecyclerView()
        initializeSearchView()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbLocalizaAnimal.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Localizar animal"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun onAnimalItemClick(animal: LocalizaAnimalResultModel) {
        val intent = Intent(this, LocalizaMapsActivity::class.java)
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    private fun initializeRecyclerView() {
        recycleViewLocAnimal = findViewById(R.id.LocRecyclerView)
        locAnimalList = ArrayList()
        locAnimalAdapter = LocalizaAnimalResultAdapter(locAnimalList, ::onAnimalItemClick)
        recycleViewLocAnimal.adapter = locAnimalAdapter
    }

    private fun initializeSearchView() {
        searchView = findViewById(R.id.idSV)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Chamada para realizar a consulta ao Firestore com o ID informado
                locListaAnimalCloud(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    private fun locListaAnimalCloud(IdAnimal: String) {
        val refAnimal = bancoDados
            .collection("Animais")
            .document(IdAnimal)

        refAnimal.addSnapshotListener { valor, erro ->
            val dadosLocAnimal = valor?.data
            if (dadosLocAnimal != null) {
                locAnimalList.clear()
                val codAnimal = dadosLocAnimal["codigo"]
                val racaAnimal = dadosLocAnimal["raca"]
                val animalModel = LocalizaAnimalResultModel(codAnimal.toString(),racaAnimal.toString(), "182 metros")
                locAnimalList.add(animalModel)
            }
            locAnimalAdapter?.notifyDataSetChanged()
        }
    }
}