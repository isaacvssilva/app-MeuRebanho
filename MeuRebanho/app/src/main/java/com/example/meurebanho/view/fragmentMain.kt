package com.example.meurebanho.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.example.meurebanho.R
import com.example.meurebanho.model.Animal
import com.example.meurebanho.model.Character

class fragmentMain: Fragment() {
    companion object{
        fun newInstance()= fragmentMain()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main,container,false);
        val character = arguments?.getSerializable("detail") as Animal
        (view.findViewById(R.id.image) as ImageView).setImageResource(character.imageResId)
        (view.findViewById(R.id.name) as TextView).text =character.codigo
        (view.findViewById(R.id.description) as TextView).text=character.raca

        return view
    }

//    fun showMessage(message:String){
//        Toast.makeText(activity,message, Toast.LENGTH_LONG).show();
//    }
}