package com.example.meurebanho.rebanhoDAO
import android.content.Context;

import java.util.ArrayList;
import com.example.meurebanho.model.Animal;

interface rebanhoDAOinterface {

        fun get_Instance(context:Context): rebanhoDAOinterface? = null;

        fun addAnimal (c:Animal):Boolean;
        fun editAnimal( c:Animal ):Boolean;
        fun deleteAnimal( codigo : String):Boolean;
        fun getAnimal( codigo:String ):Animal?;
        fun getListaanimais():ArrayList<Animal>;
        fun deleteAll():Boolean;

        fun init():Boolean;
        fun close():Boolean;
        fun isStarted():Boolean;
}