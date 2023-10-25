package com.example.meurebanho.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.meurebanho.R

class LocalizaAnimalResultAdapter(
    private var locAnimalList: List<LocalizaAnimalResultModel>,
    private val onItemClick: (LocalizaAnimalResultModel) -> Unit
) : RecyclerView.Adapter<LocalizaAnimalResultAdapter.LocAnimalMyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocAnimalMyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_localiza_animal, parent, false)
        return LocAnimalMyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return locAnimalList.size
    }

    override fun onBindViewHolder(holder: LocAnimalMyViewHolder, position: Int) {
        val item = locAnimalList[position]
        holder.locRacaAnimal.text = item.raca
        holder.locDistanciaAnimal.text = item.distancia

        /* Pegando os dados do animal consultado */
        val dataAnimal = locAnimalList[position]

        /* Capturando evento de click no CardView */
        holder.cdViewAnimal.setOnClickListener {
            onItemClick(dataAnimal)
        }
    }

    class LocAnimalMyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val locImgAnimal: ImageView = view.findViewById(R.id.locImgAnimal)
        val locRacaAnimal: TextView = view.findViewById(R.id.locRacaAnimal)
        val locDistanciaAnimal: TextView = view.findViewById(R.id.locDistanciaAnimal)
        val cdViewAnimal: CardView = itemView.findViewById(R.id.cardViewLocAnimal)
    }
}
