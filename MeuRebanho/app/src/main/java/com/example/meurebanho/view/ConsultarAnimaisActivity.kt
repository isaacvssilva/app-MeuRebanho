package com.example.meurebanho.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meurebanho.R
import com.example.meurebanho.controller.codes.Codes
import com.example.meurebanho.model.Animal
import com.example.meurebanho.rebanhoDAO.rebanhoDAO
import com.example.meurebanho.rebanhoDAO.rebanhoDAOinterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Collections


class ConsultarAnimaisActivity:AppCompatActivity() {

    private lateinit var animalDAO: rebanhoDAOinterface

    companion object {
        var animais = ArrayList<Animal>()
        private lateinit var adapterani: AnimalAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_animais)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        initToolbal()

//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .add(
//                    R.id.container_root, fragmentlist.newInstance(),
//                    "fragmentlist"
//                )
//                .commit()
        animalDAO = rebanhoDAO.getInstance(this)

        animalDAO.init()

        if (!animais.isEmpty())
            animais.clear() 

        animais = animalDAO.getListaanimais()

        adapterani = AnimalAdapter(this, animais)
        recyclerView.adapter = adapterani
        recyclerView.layoutManager = LinearLayoutManager(this)

        val helper = androidx.recyclerview.widget.ItemTouchHelper(
            ItemTouchHelper(
                androidx.recyclerview.widget.ItemTouchHelper.UP
                        or androidx.recyclerview.widget.ItemTouchHelper.DOWN,
                0
            )
        )
        helper.attachToRecyclerView(recyclerView)


        var buttonadd = findViewById<FloatingActionButton>(R.id.add_animal)

        buttonadd.setOnClickListener {
            val intent = Intent(this, CadastroAnimalActivity::class.java)
            startActivityForResult(intent, Codes.REQUEST_ADD)
        }

    }

    inner class ItemTouchHelper(dragDirs: Int, swipeDirs: Int) :
        androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
            dragDirs, swipeDirs
        ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from: Int = viewHolder.adapterPosition
            val to: Int = target.adapterPosition
            Collections.swap(adapterani.animais, from, to)
            adapterani.notifyItemMoved(from, to)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapterani.animais.removeAt(viewHolder.adapterPosition)
            adapterani.notifyItemRemoved(viewHolder.adapterPosition)
        }
    }

    private fun initToolbal() {
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater;
        inflater.inflate(R.menu.menu_consultar_animais, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun notifyAdapter() {
        Log.d("tamlist", adapterani.itemCount.toString())
        adapterani.notifyDataSetChanged()
    }

    fun editarAnimal(pos: Int) {
        val c: Animal = animais[pos]

        val intent = Intent(this, CadastroAnimalActivity::class.java)

        intent.putExtra(Codes.CHAVE_CODIGO, c.codigo)
        intent.putExtra(Codes.CHAVE_ESPECIE, c.especie)
        intent.putExtra(Codes.CHAVE_RACA, c.raca)
        intent.putExtra(Codes.CHAVE_COR, c.cor)
        intent.putExtra(Codes.CHAVE_DATANASC, c.datanasc)
        intent.putExtra(Codes.CHAVE_SEXO, c.sexo)
        intent.putExtra(Codes.CHAVE_DOCID, c.documentID)
        startActivityForResult(intent, Codes.REQUEST_EDT)
    }

    fun confirm_remove_Animal(A: Animal) {
        openDialog(A)
    }

    fun remove_animal(A: Animal) {
        animalDAO.deleteAnimal(A.documentID)
    }

    fun openDialog(A: Animal) {
        var dialog: Dialog = Dialog(this)
        var layoutInflater: LayoutInflater = this.layoutInflater
        val customdialog: View = layoutInflater.inflate(R.layout.caixa_confirmacao, null)
        dialog.setContentView(customdialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val button = customdialog.findViewById<Button>(R.id.btn_confirmar)
        button.setOnClickListener {
            remove_animal(A)
            dialog.dismiss()
        }
        val buttoncancelar = customdialog.findViewById<Button>(R.id.btn_cancelar)
        buttoncancelar.setOnClickListener {
            dialog.cancel()
        }
        dialog.show()
    }

    fun abrir_detalhes_animal(pos: Int){
        val c: Animal = animais[pos]
        val intent = Intent(this, DetailAnimal::class.java)
        intent.putExtra(Codes.CHAVE_CODIGO, c.codigo)
        intent.putExtra(Codes.CHAVE_ESPECIE, c.especie)
        intent.putExtra(Codes.CHAVE_RACA, c.raca)
        intent.putExtra(Codes.CHAVE_COR, c.cor)
        intent.putExtra(Codes.CHAVE_DATANASC, c.datanasc)
        intent.putExtra(Codes.CHAVE_SEXO, c.sexo)
        intent.putExtra(Codes.CHAVE_DOCID, c.documentID)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && resultCode == Codes.RESPONSE_OK) {

            var datanasc: String = data.extras!!.getString(Codes.CHAVE_DATANASC)!!
            var especie: String = data.extras!!.getString(Codes.CHAVE_ESPECIE)!!
            var raca: String = data.extras!!.getString(Codes.CHAVE_RACA)!!
            var cor: String = data.extras!!.getString(Codes.CHAVE_COR)!!
            var codigo: String = data.extras!!.getString(Codes.CHAVE_CODIGO)!!
            var sexo: String = data.extras!!.getString(Codes.CHAVE_SEXO)!!

            val animal = Animal(
                raca,
                especie,
                cor,
                sexo,
                datanasc,
                codigo,
                "-1",
                R.drawable.nelore1
            )

            if (requestCode == Codes.REQUEST_ADD)
                animalDAO.addAnimal(animal)
            else if (requestCode == Codes.REQUEST_EDT) {
                var codigoedt:String=data.extras!!.getString(Codes.CHAVE_DOCID)!!
                animal.documentID=codigoedt
                animalDAO.editAnimal(animal)
            }

            adapterani.notifyDataSetChanged()

        }
    }


//    override fun onSelected(character: Animal) {
//    val args= Bundle()
//    args.putSerializable("detail",character)
//    val fragment =fragmentMain.newInstance()
//    fragment.arguments= args
//    supportFragmentManager
//        .beginTransaction()
//        .replace(R.id.container_root,fragment,"fragmentDetail")
//        .addToBackStack(null)
//        .commit()
//}
}