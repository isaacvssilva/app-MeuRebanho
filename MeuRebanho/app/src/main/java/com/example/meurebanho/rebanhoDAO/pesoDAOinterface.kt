package com.example.meurebanho.rebanhoDAO

import android.content.Context
import com.example.meurebanho.model.Animal
import com.example.meurebanho.model.PesoAnimal
import java.util.ArrayList

interface pesoDAOinterface {
    fun get_Instance(context: Context): pesoDAOinterface? = null;
    fun addPeso(a: PesoAnimal):Boolean;
    fun editPeso( a: PesoAnimal):Boolean;
    fun deletePeso( a : String):Boolean;
    fun getPeso( codigo:String ): PesoAnimal?;
    fun getListaPesos(document_id:String): ArrayList<PesoAnimal>;
    fun init():Boolean;
    fun close():Boolean;
    fun isStarted():Boolean;
}