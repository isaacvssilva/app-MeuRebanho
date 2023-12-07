package com.example.meurebanho.view

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meurebanho.R
import com.example.meurebanho.controller.codes
import com.example.meurebanho.databinding.ActivityCadastroAnimalBinding
import com.example.meurebanho.databinding.ActivityListPesosAnimaisBinding
import com.example.meurebanho.model.Animal
import com.example.meurebanho.model.PesoAnimal
import com.example.meurebanho.rebanhoDAO.pesoDAO
import com.example.meurebanho.rebanhoDAO.pesoDAOinterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Locale

class ListPesosAnimais:AppCompatActivity() {

        private lateinit var animalpesoDAO: pesoDAOinterface

     val formatDAte = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        companion object {
            var pesos = ArrayList<PesoAnimal>()
            private lateinit var adapterpeso: PesoAdapter
            private lateinit var codigo_animal:String
            private lateinit var document_id_animal:String
        }

        private lateinit var binding: ActivityListPesosAnimaisBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityListPesosAnimaisBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_peso)
            initToolbal()

            document_id_animal=intent.extras!!.getString(codes.Codes.CHAVE_DOCID).toString()
            codigo_animal=intent.extras!!.getString(codes.Codes.CHAVE_CODIGO).toString()

            animalpesoDAO = pesoDAO.getInstance(this)

            animalpesoDAO.init()

            if (!pesos.isEmpty())
                pesos.clear()

            pesos = animalpesoDAO.getListaPesos(document_id_animal)

            adapterpeso = PesoAdapter(this, pesos)
            recyclerView.adapter = adapterpeso
            recyclerView.layoutManager = LinearLayoutManager(this)

            val helper = androidx.recyclerview.widget.ItemTouchHelper(
                ItemTouchHelper(
                    androidx.recyclerview.widget.ItemTouchHelper.UP
                            or androidx.recyclerview.widget.ItemTouchHelper.DOWN,
                    0
                )
            )
            helper.attachToRecyclerView(recyclerView)


            var buttonadd = findViewById<FloatingActionButton>(R.id.btn_add_peso)

            buttonadd.setOnClickListener {
                val intent = Intent(this, CadastroPesoActivity::class.java)
                intent.putExtra(codes.Codes.CHAVE_DOCID,document_id_animal)
                intent.putExtra(codes.Codes.CHAVE_CODIGO,codigo_animal)
                startActivityForResult(intent, codes.Codes.REQUEST_ADD)
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
                Collections.swap(adapterpeso.pesos, from, to)
                adapterpeso.notifyItemMoved(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapterpeso.pesos.removeAt(viewHolder.adapterPosition)
                adapterpeso.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }

        private fun initToolbal() {
            var toolbar= binding.tbListPesoAni.toolbar
            setSupportActionBar(toolbar)
            if (supportActionBar != null) {
                supportActionBar!!.title = "Gestão de Pesos"
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            }
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            var inflater: MenuInflater = menuInflater;
            inflater.inflate(R.menu.menu_consultar_animais, menu)
            return super.onCreateOptionsMenu(menu)
        }

        fun notifyAdapterpesos() {
            Log.d("tamlist", adapterpeso.itemCount.toString())
            adapterpeso.notifyDataSetChanged()
        }

        fun editarPesoAnimal(pos: Int) {
            val c: PesoAnimal = pesos[pos]

            val intent = Intent(this, CadastroPesoActivity::class.java)
            intent.putExtra(codes.Codes.CHAVE_DOCID, c.document_id_animal)
            intent.putExtra(codes.Codes.CHAVE_CODIGO, c.codigo_animal)
            intent.putExtra(codes.Codes.CHAVE_PESOID, c.document_id_peso)
            intent.putExtra(codes.Codes.CHAVE_PESO, c.peso)
            intent.putExtra(codes.Codes.CHAVE_DATAPESO, formatDAte.format(c.data_peso))

            startActivityForResult(intent, codes.Codes.REQUEST_EDT)
        }

        fun confirm_remove_Peso_Animal(A: PesoAnimal) {
            openDialog(A)
        }

        fun remove_peso(A: PesoAnimal) {
            animalpesoDAO.deletePeso(A.document_id_peso)
        }

        fun openDialog(A: PesoAnimal) {
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
                remove_peso(A)
                dialog.dismiss()
            }
            val buttoncancelar = customdialog.findViewById<Button>(R.id.btn_cancelar)
            buttoncancelar.setOnClickListener {
                dialog.cancel()
            }
            dialog.show()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (data != null && resultCode == codes.Codes.RESPONSE_OK) {
                val formatDAte = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                var document_id= data.extras!!.getString(codes.Codes.CHAVE_DOCID)!!
                var codigo: String = data.extras!!.getString(codes.Codes.CHAVE_CODIGO)!!
                var datapeso: String = data.extras!!.getString(codes.Codes.CHAVE_DATAPESO)!!
                var peso: Float = data.extras!!.getFloat(codes.Codes.CHAVE_PESO)!!
                var peso_id: String = data.extras!!.getString(codes.Codes.CHAVE_PESOID)!!

                val data_peso_formatada=formatDAte.parse(datapeso)

                val pesoanimal = PesoAnimal(
                    codigo,
                    peso,
                    data_peso_formatada,
                    document_id,
                    peso_id
                )

                if (requestCode == codes.Codes.REQUEST_ADD) {
                    animalpesoDAO.addPeso(pesoanimal)
                }else if (requestCode == codes.Codes.REQUEST_EDT) {
                    animalpesoDAO.editPeso(pesoanimal)
                }

                adapterpeso.notifyDataSetChanged()

            }
        }

}