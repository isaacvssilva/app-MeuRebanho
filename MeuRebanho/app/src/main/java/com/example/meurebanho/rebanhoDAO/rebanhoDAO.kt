package com.example.meurebanho.rebanhoDAO
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity
import com.example.meurebanho.R
import com.example.meurebanho.view.CadastroAnimalActivity

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


import com.example.meurebanho.model.Animal;
import com.example.meurebanho.view.ConsultarAnimaisActivity
import com.example.meurebanho.view.fragmentlist
import com.google.firebase.firestore.QueryDocumentSnapshot


class rebanhoDAO():rebanhoDAOinterface {

    private lateinit var db: FirebaseFirestore;
    lateinit var lista:ArrayList<Animal>

    companion object {
        private lateinit var mainActivity: fragmentlist
        private var animaisDAOFirebase: rebanhoDAO? = null;

        fun getInstance(mainActivity: fragmentlist):rebanhoDAOinterface{
            if( animaisDAOFirebase == null ){
                animaisDAOFirebase = rebanhoDAO( mainActivity );
            }
            return animaisDAOFirebase as rebanhoDAO;
        }
    }

    private constructor(mainactivity: fragmentlist) : this() {
        rebanhoDAO.mainActivity = mainactivity;
        lista = ArrayList();
    }
        override fun addAnimal(animal:Animal):Boolean {
            //mainActivity.controlProgressBar( true );
            val user = hashMapOf<String,Any>()
            user.put("nome", animal.especie);
            user.put("raca", animal.raca);
            user.put("cor", animal.cor);
            user.put("sexo", animal.sexo);
            user.put("datanasc", animal.datanasc);
            user.put("codigo", animal.codigo);

            // Add a new document with a generated ID
            //var documento:DocumentReference
            db.collection("Animais")
                .document( animal.codigo )
                .set( user )
                .addOnSuccessListener{
//                        Toast.makeText( mainActivity, animal.codigo , Toast.LENGTH_LONG ).show();
                        lista.add( animal );
                        //mainActivity.notifyAdapter();
//                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();
                }
                .addOnFailureListener {
//                        Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();
                };

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
//                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();

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
//                        Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();
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

                    fun onSuccess() {
//                        Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();

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
                        fun onFailure( e:Exception) {
//                            Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();

                        }
                    });

            }

            return false;
        }

        @Override
        override fun getAnimal(animalId:String): Animal? {
            return null;
        }

        override fun getListaanimais():ArrayList<Animal> {

            //mainActivity.controlProgressBar( true );
            db.collection("Animais")
                .get()
                .addOnCompleteListener( OnCompleteListener {
                    if(it.isSuccessful) {
//                        lista = ArrayList<Animal>();
//                        Toast.makeText(mainActivity, "dados capturados", Toast.LENGTH_SHORT).show();
                            for (data in it.result) {
                                val codigo = data.getString("codigo").toString();
                                val especie: String = data.getString("nome").toString();
                                val raca: String = data.getString("raca").toString();
                                val cor: String = data.getString("cor").toString();
                                val sexo: String = data.getString("sexo").toString();
                                val datanasc: String = data.getString("datanasc").toString();

                                val c: Animal = Animal(
                                    codigo,
                                    especie,
                                    raca,
                                    cor,
                                    sexo,
                                    datanasc,
                                    (R.drawable.nelore)
                                );
                                c.codigo = data.id;

                                lista.add(c);
                            }
//                            Toast.makeText(
//                                mainActivity,
//                                lista[1].codigo.toString(),
//                                Toast.LENGTH_SHORT
//                            )
//                                .show();
                        } else {
                            Log.e("TAG", "erro ao receber.");
                    }
                    mainActivity.notifyAdapter()
                    }).addOnFailureListener{
//                    Toast.makeText( mainActivity, it.toString(), Toast.LENGTH_SHORT ).show();
                }
            return lista
        }

    override fun deleteAll(): Boolean= false;

    override fun init(): Boolean {
        db = FirebaseFirestore.getInstance();
        return true;
    }

    override fun close(): Boolean=false;
    override fun isStarted(): Boolean = false;

}



