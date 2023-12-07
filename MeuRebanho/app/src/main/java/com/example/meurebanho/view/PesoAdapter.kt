package com.example.meurebanho.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.meurebanho.R
import com.example.meurebanho.model.Animal
import com.example.meurebanho.model.PesoAnimal
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Locale

class PesoAdapter(val activity: ListPesosAnimais,val pesos:MutableList<PesoAnimal>): RecyclerView.Adapter<PesoAdapter.ViewHolderPesoAnimal>() {
    companion object variaveis {
        var dataset = ArrayList<PesoAnimal>()
    }
    private lateinit var btn_delete: ImageButton
    private lateinit var recicler: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        ViewHolderPesoAnimal(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item_list_peso,parent,false)
        )


    override fun getItemCount():Int = pesos.size

    override fun onBindViewHolder(holder: ViewHolderPesoAnimal, position: Int) {
        holder.bind(pesos[position])
    }

    inner class ViewHolderPesoAnimal (itemView: View):
        RecyclerView.ViewHolder(itemView){
        fun bind(character: PesoAnimal){
            val formatDAte = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val listcodigo: TextView = itemView.findViewById(R.id.list_codigo_peso)
            val listdatapeso: TextView= itemView.findViewById(R.id.list_datapeso)
            val listpeso: TextView = itemView.findViewById(R.id.list_peso)

            val button_delete: ImageButton = itemView.findViewById(R.id.button_delete_peso)
            val button_edit: ImageButton = itemView.findViewById(R.id.button_edit_peso)


            listcodigo.text="N°"+character.codigo_animal
            listdatapeso.text= "Data Pesagem: "+formatDAte.format(character.data_peso)
            listpeso.text="Peso: "+character.peso.toString()

            button_delete.setOnClickListener{
                activity.confirm_remove_Peso_Animal(character)
            }
            button_edit.setOnClickListener{
                activity.editarPesoAnimal(adapterPosition)
            }
        }
    }

}
