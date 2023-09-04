package com.example.meurebanho

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

/* ViewHolder -> usado para gerenciar a posicao das listas */
//class MenuItemAdapter : RecyclerView.Adapter<MenuItemAdapter.MenuItemAdapterViewHolder>() {
//
//    private val list = mutableListOf<MenuItemModel>()
//
//    class MenuItemAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//        /* colocando titulos no menu (layout) */
//        private val tvTitle by lazy{
//            itemView.findViewById<TextView>(R.id.TextViewMenu)
//        }
//
//        //setando o titulo
//        fun iniciaViews(item: MenuItemModel){
//            tvTitle.text = item.titulo
//        }
//    }
//
//    //criacao do layout
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemAdapterViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_menu, parent, false)
//        return MenuItemAdapterViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: MenuItemAdapterViewHolder, position: Int) {
//        holder.iniciaViews(list[position])
//    }
//
//    //verifica a quantidade de itens
//    override fun getItemCount(): Int {
//        return list.size
//    }
//
//    //limpa a lista e adiciona todos os itens de novo
//    fun setItensList(list: List<MenuItemModel>) {
//        this.list.clear()
//        this.list.addAll(list)
//    }
//}

class MenuItemAdapter(
    /**
     * @param getActivity Referencia a activity MenuActivity que esta usando este adaptador.
     * @param menuList A lista de dados do menu a ser exibida no RecyclerView.
     * @param onItemClick Um lambda que sera acionado quando um item do menu for clicado.
     */

    private val getActivity: MenuActivity,
    private val menuList: List<MenuData>,
    private val onItemClick: (MenuData) -> Unit
) : RecyclerView.Adapter<MenuItemAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuItemAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        /* Definindo o texto e imagem do item do menu, baseado na lista  */
        holder.tv_menu_titulo.text = menuList[position].titulo
        holder.iv_menu_img.setImageResource(menuList[position].imagem)

        /* Pegando os dados da posicao atual do item do menu */
        val menuData = menuList[position]

        /* Capturando evento de click no CardView para abrir uma nova activity */
        holder.cdView.setOnClickListener {
            onItemClick(menuData)
        }
    }

    override fun getItemCount(): Int {
        return menuList.size
    }
    /* ViewHolder para manter as referencias dos elementos do item do Menu */
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val tv_menu_titulo: TextView = itemView.findViewById(R.id.TextViewMenu)
        val iv_menu_img: ImageView = itemView.findViewById(R.id.ImageViewMenu)
        val cdView: CardView = itemView.findViewById(R.id.cardViewMenu)
    }
}