package com.example.meurebanho

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/* ViewHolder -> usado para gerenciar a posicao das listas */
class MenuItemAdapter : RecyclerView.Adapter<MenuItemAdapter.MenuItemAdapterViewHolder>() {

    private val list = mutableListOf<MenuItemModel>()

    class MenuItemAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /* colocando titulos no menu (layout) */
        private val tvTitle by lazy{
            itemView.findViewById<TextView>(R.id.TextViewMenu)
        }

        //setando o titulo
        fun iniciaViews(item: MenuItemModel){
            tvTitle.text = item.titulo
        }
    }

    //criacao do layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
        return MenuItemAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuItemAdapterViewHolder, position: Int) {
        holder.iniciaViews(list[position])
    }

    //verifica a quantidade de itens
    override fun getItemCount(): Int {
        return list.size
    }

    //limpa a lista e adiciona todos os itens de novo
    fun setItensList(list: List<MenuItemModel>) {
        this.list.clear()
        this.list.addAll(list)
    }
}