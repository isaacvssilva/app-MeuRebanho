package com.example.meurebanho.rebanhoDAO
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.example.meurebanho.MainActivity
import com.example.meurebanho.model.Animal;
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.Objects

class rebanhoDAO():rebanhoDAOinterface {

    private lateinit var db: FirebaseFirestore;
    var lista = ArrayList<Animal>()

    companion object {
        private lateinit var mainActivity: MainActivity
        private var animaisDAOFirebase: rebanhoDAO? = null;

        fun getInstance( mainActivity:MainActivity  ):rebanhoDAOinterface{
            if( animaisDAOFirebase == null ){
                animaisDAOFirebase = rebanhoDAO( mainActivity );
            }
            return animaisDAOFirebase as rebanhoDAO;
        }
    }

    private constructor(mainactivity: MainActivity) : this() {
        rebanhoDAO.mainActivity = mainactivity;
        lista = ArrayList();
    }
        override fun addAnimal(animal:Animal):Boolean {

            //mainActivity.controlProgressBar( true );
            var user = hashMapOf<String,Any>()
            user.put("nome", animal.especie);
            user.put("nome", animal.raca);
            user.put("nome", animal.cor);
            user.put("nome", animal.sexo);
            user.put("nome", animal.datanasc);
            user.put("nome", animal.codigo);

            // Add a new document with a generated ID
            db.collection("animais")
                .add(user)
                .addOnSuccessListener(OnSuccessListener<DocumentReference>() {

                    fun onSuccess(documentReference: DocumentReference ):Unit {
                        Log.d( "Sucess", "DocumentSnapshot added with ID: " + documentReference.id);

                        animal.codigo=documentReference.id ;
                        lista.add( animal );
                        //mainActivity.notifyAdapter();

                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();
                    }
                })
                .addOnFailureListener(OnFailureListener() {

                    fun onFailure( e:Exception){
                        Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();
                    }
                });

            //mainActivity.controlProgressBar( false );

            return true;

        }

    override fun editAnimal(c: Animal): Boolean {
            var newAnimal:DocumentReference  = db.collection("Animais").document( c.codigo );
            newAnimal.update("codigo", c.codigo,
                "especie", c.especie,
                "datanasc", c.datanasc )
                .addOnSuccessListener(OnSuccessListener<Void>() {
                    fun onSuccess() {
                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();

                        for( animal:Animal in lista ){

                        if(animal.codigo == c.codigo){
                            animal.especie= c.especie;
                            animal.raca= c.raca;
                            animal.cor= c.cor;
                            animal.sexo= c.sexo;
                            animal.datanasc= c.datanasc;
                            animal.codigo= c.codigo;

                            //mainActivity.notifyAdapter();

                            break;

                        }
                    }

                        //mainActivity.notifyAdapter();
                    }
                })
                .addOnFailureListener(OnFailureListener() {
                    @Override
                    fun onFailure( e:Exception) {
                        Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();
                        //mainActivity.notifyAdapter();
                    }
                });

            return false;
        }

    override fun deleteAnimal(codigoanimal: String):Boolean{
            var c:Animal? = null;

            for( animal:Animal in lista ){
            if( animal.codigo == codigoanimal) {
                c = animal;
                break;
            }
        }

            if( c != null ){

               val apagar:Animal= c;

                 var deleteanimal:DocumentReference = db.collection("Animais").document( c.codigo );
                deleteanimal.delete().addOnSuccessListener( OnSuccessListener<Void>() {
                    @Override
                    fun onSuccess() {
                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();

                        var animalapagar:Animal? = null;
                        for( animal:Animal in lista ){

                        if( animal.codigo== apagar.codigo ){
                            animalapagar = animal;
                            break;
                        }

                    }

                        if( animalapagar != null ) lista.remove( animalapagar );
                        //mainActivity.notifyAdapter();
                    }
                })
                    .addOnFailureListener(OnFailureListener() {
                        @Override
                        fun onFailure( e:Exception) {
                            Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();

                        }
                    });

            }

            return false;
        }

        @Override
        override fun getAnimal(animalId:String): Animal? {
            return null;
        }

        override fun getListaanimais():ArrayList<Animal>{

            //mainActivity.controlProgressBar( true );

            lista = ArrayList<Animal>();

            db.collection("Animais")
                .get()
                .addOnCompleteListener(OnCompleteListener<QuerySnapshot>() {

                    fun onComplete(task:Task<QuerySnapshot> ) {
                        if (task.isSuccessful) {

                            for (document: QueryDocumentSnapshot  in task.result) {

                                var codigo: String = document.getString( "codigo").toString();
                                var especie: String = document.getString( "especie").toString();
                                var raca: String = document.getString( "raca").toString();
                                var cor: String = document.getString( "cor").toString();
                                var sexo: String = document.getString( "sexo").toString();
                                var datanasc: String = document.getString( "datanasc").toString();

                                var c:Animal= Animal( codigo,especie,raca,cor,sexo,datanasc);
                                c.codigo= document.id ;

                                lista.add( c );

                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }

                        //mainActivity.notifyAdapter();
                    }
                });

            //mainActivity.controlProgressBar( false );

            return lista;

        }

    override fun deleteAll(): Boolean= false;

    override fun init(): Boolean {
        db = FirebaseFirestore.getInstance();
        return true;
    }

    override fun close(): Boolean= false;
    override fun isStarted(): Boolean = false;

}

