package com.example.meurebanho.view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.meurebanho.R
import com.example.meurebanho.controller.codes
import com.example.meurebanho.model.Animal
import com.google.android.material.imageview.ShapeableImageView

class AnimalAdapter(val activity: ConsultarAnimaisActivity,val animais:MutableList<Animal>): RecyclerView.Adapter<AnimalAdapter.ViewHolderAnimal>() {
    companion object variaveis {
        var dataset = ArrayList<Animal>()
    }
    private lateinit var btn_delete:ImageButton
//    private val itemClickListener:OnAnimalClickListener
    private lateinit var recicler:ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        ViewHolderAnimal(
            LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        )


    override fun getItemCount():Int = animais.size

    override fun onBindViewHolder(holder: ViewHolderAnimal, position: Int) {
            holder.bind(animais[position])
        }

inner class ViewHolderAnimal (itemView: View):
    RecyclerView.ViewHolder(itemView){
    fun bind(character:Animal){
        val listcodigo: TextView = itemView.findViewById(R.id.list_codigo)
        val listimg: ShapeableImageView = itemView.findViewById(R.id.list_img)
        val listcor: TextView = itemView.findViewById(R.id.list_cor)
        val listraca: TextView = itemView.findViewById(R.id.list_raca)
        val button_delete: ImageButton = itemView.findViewById(R.id.button_delete)
        val button_edit: ImageButton = itemView.findViewById(R.id.button_edit)

        val cardview:CardView =itemView.findViewById(R.id.item_animal)


        listcodigo.text="N°"+character.codigo
        listimg.setImageResource(R.drawable.nelore1)
        listraca.text=character.raca
        listcor.text=character.cor

        cardview.setOnClickListener{
            activity.abrir_detalhes_animal(adapterPosition)
        }

        button_delete.setOnClickListener{
            activity.confirm_remove_Animal(character)
        }
        button_edit.setOnClickListener{
            activity.editarAnimal(adapterPosition)
        }


    }
}

}