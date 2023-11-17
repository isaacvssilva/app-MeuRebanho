package com.example.meurebanho.rebanhoDAO
import android.util.Log;
import android.widget.Toast;
import com.example.meurebanho.R

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


import com.example.meurebanho.model.Animal;
import com.example.meurebanho.view.ConsultarAnimaisActivity
import com.google.firebase.database.FirebaseDatabase


class rebanhoDAO():rebanhoDAOinterface {

    private lateinit var db: FirebaseFirestore;
    lateinit var lista:ArrayList<Animal>

    companion object {
        private lateinit var mainActivity: ConsultarAnimaisActivity
        private var animaisDAOFirebase: rebanhoDAO? = null;

        fun getInstance(mainActivity: ConsultarAnimaisActivity):rebanhoDAOinterface{
            if( animaisDAOFirebase == null ){
                animaisDAOFirebase = rebanhoDAO( mainActivity );
            }
            return animaisDAOFirebase as rebanhoDAO;
        }
    }

    private constructor(mainactivity: ConsultarAnimaisActivity) : this() {
        mainActivity = mainactivity;
        lista = ArrayList();
    }
        override fun addAnimal(animal:Animal):Boolean {
            //mainActivity.controlProgressBar( true );
            val user = hashMapOf<String,Any>()
            user.put("raca", animal.raca);
            user.put("especie",animal.especie)
            user.put("cor", animal.cor);
            user.put("sexo", animal.sexo);
            user.put("datanasc", animal.datanasc);
            user.put("codigo", animal.codigo);

            // Add a new document with a generated ID
            //var documento:DocumentReference
            db.collection("Animais")
                .add( user )
                .addOnSuccessListener{
                        //Toast.makeText( mainActivity, animal.codigo , Toast.LENGTH_LONG ).show();
                        animal.documentID= it.id
                        lista.add( animal );
                        /* criando nó no Firebase Realtime Database */
                        nodeFilhoAnimalRTDB(animal.documentID)
                        mainActivity.notifyAdapter();
                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();
                }
                .addOnFailureListener {
                        Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();
                };

            //mainActivity.controlProgressBar( false );
            return true;

        }

    override fun editAnimal(c: Animal): Boolean {
            var newAnimal:DocumentReference  = db.collection("Animais").document( c.documentID );
            newAnimal.update("codigo", c.codigo,
                "raca",c.raca,
                "especie",c.especie,
                "cor",c.cor,
                "sexo",c.sexo,
                "datanasc", c.datanasc )
                .addOnSuccessListener{
                    Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();

                        for( animal:Animal in lista ){

                        if(animal.documentID == c.documentID){
                            animal.raca= c.raca;
                            animal.especie=c.especie
                            animal.cor= c.cor;
                            animal.sexo= c.sexo;
                            animal.datanasc= c.datanasc;
                            animal.codigo= c.codigo;

                            break;

                        }
                    }
                    mainActivity.notifyAdapter();
                }
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
            if( animal.documentID == codigoanimal) {
                c = animal;
                break;
            }
        }

            if( c != null ){

               val apagar:Animal= c;

                 var deleteanimal:DocumentReference = db.collection("Animais").document( c.documentID );
                deleteanimal.delete().addOnSuccessListener( OnSuccessListener<Void>() {
                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();
                        var animalapagar:Animal? = null;
                        for( animal:Animal in lista ){
                        if( animal.documentID== apagar.documentID ){
                            animalapagar = animal;
                        }
                        if( animalapagar != null ) {
                            lista.remove( animalapagar );
                            mainActivity.notifyAdapter();
                            break
                        }
                    }
                })
                    .addOnFailureListener(OnFailureListener() {
                        fun onFailure( e:Exception) {
                            Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();

                        }
                    });

            }

            return false;
        }

        override fun getAnimal(animalId:String): Animal? {
            return null;
        }

        override fun getListaanimais():ArrayList<Animal> {

            //mainActivity.controlProgressBar( true );
            db.collection("Animais")
                .get()
                .addOnCompleteListener( OnCompleteListener {
                    if(it.isSuccessful) {
//                        Toast.makeText(mainActivity, "dados capturados", Toast.LENGTH_SHORT).show();
                            for (data in it.result) {
                                val codigo = data.getString("codigo").toString();
                                val raca: String = data.getString("raca").toString();
                                val especie:String =data.getString("especie").toString();
                                val cor: String = data.getString("cor").toString();
                                val sexo: String = data.getString("sexo").toString();
                                val datanasc: String = data.getString("datanasc").toString();

                                val c: Animal = Animal(
                                    raca,
                                    especie,
                                    cor,
                                    sexo,
                                    datanasc,
                                    codigo,
                                    data.id,
                                    (R.drawable.nelore1)
                                );
                                //c.codigo = data.id;

                                lista.add(c);
                            }
                        } else {
                            Log.e("TAG", "erro ao receber.");
                    }
                    mainActivity.notifyAdapter()
                    }).addOnFailureListener{
                     Toast.makeText( mainActivity, it.toString(), Toast.LENGTH_SHORT ).show();
                }
            return lista
        }

    override fun deleteAll(): Boolean= false;

    override fun init(): Boolean {
        db = FirebaseFirestore.getInstance();
        return true;
    }
    private fun nodeFilhoAnimalRTDB(animalId: String) {
        /* Obtendo uma referencia ao no "Animal" no Firebase Realtime Database */
        val database = FirebaseDatabase.getInstance()
        val animalRef = database.getReference("Animal/$animalId/")

        /* Criando um novo no com o ID do animal como chave */
        val novoAnimalRef = animalRef.child("GPS")

        /* Adicionando os dados ao novo nó */
        novoAnimalRef.setValue("")
    }

    override fun close(): Boolean=false;
    override fun isStarted(): Boolean = false;

}



