package com.example.meurebanho.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meurebanho.R
import com.example.meurebanho.model.Animal
import com.example.meurebanho.model.Character
import com.example.meurebanho.rebanhoDAO.rebanhoDAO
import com.example.meurebanho.rebanhoDAO.rebanhoDAOinterface
import com.google.android.material.imageview.ShapeableImageView
import java.lang.ClassCastException
import java.util.ArrayList

class fragmentlist : Fragment(){

//    private lateinit var listener: fragmentlist.OnlistSelected
//    //private var  imageResIds:IntArray= intArrayOf(R.drawable.add_animal,R.drawable.rota_loc,R.drawable.graficos)
//    private lateinit var animalDAO: rebanhoDAOinterface
//
//    companion object{
//        var animais=ArrayList<Animal>()
//        private lateinit var adapterani:FragmentListAdapter
//        fun newInstance()=fragmentlist()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view= inflater.inflate(R.layout.fragment_list,container,false)
//        val activity =activity as Context
//        val recyclerView=view.findViewById<RecyclerView>(R.id.recycler_view)
//        recyclerView.layoutManager=LinearLayoutManager(activity)
//        if(!animais.isEmpty())
//            animais.clear()
//        animalDAO= rebanhoDAO.getInstance(fragmentlist())
//
//        animalDAO.init()
//
//        animais = animalDAO.getListaanimais()
//
//        adapterani=FragmentListAdapter()
//
//        recyclerView.adapter=adapterani
//        return view
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        if(context is OnlistSelected){
//            listener = context
//        }else{
//            throw ClassCastException("$context must implemented")
//        }
//
//    }
//    internal inner class FragmentListAdapter:
//        RecyclerView.Adapter<myViewHolder>(){
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
//            myViewHolder(
//                LayoutInflater.from(context).inflate(R.layout.item_list,parent,false)
//            )
//        override fun onBindViewHolder(holder: myViewHolder, position: Int) {
//            val animal= animais[position]
//            holder.bind(animal)
//            holder.itemView.setOnClickListener{
//                listener.onSelected(animal)
//            }
//        }
//        override fun getItemCount()= animais.size
//        }
//        internal inner class myViewHolder (itemView: View):
//            RecyclerView.ViewHolder(itemView){
//                fun bind(character:Animal){
//                    val listname:TextView = itemView.findViewById(R.id.listname)
//                    val listimg: ShapeableImageView= itemView.findViewById(R.id.list_img)
//                    val listcor: TextView = itemView.findViewById(R.id.listcor)
//                    val listraca: TextView = itemView.findViewById(R.id.listraca)
//                    val distancia: TextView = itemView.findViewById(R.id.listdistancia)
//
//                    listname.text="N°"+character.codigo
//                    listimg.setImageResource(R.drawable.nelore1)
//                    listraca.text=character.raca
//                    listcor.text=character.cor
//                    distancia.text="8km"
//                }
//            }
//    interface OnlistSelected{
//        fun onSelected(character: Animal)
//    }
//    fun notifyAdapter() {
//        Log.d("tamlist",adapterani.itemCount.toString())
//        adapterani.notifyDataSetChanged()
//    }



}