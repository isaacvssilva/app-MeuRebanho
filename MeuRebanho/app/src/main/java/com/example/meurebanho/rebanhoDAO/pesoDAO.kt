package com.example.meurebanho.rebanhoDAO

import android.util.Log
import android.widget.Toast
import com.example.meurebanho.R
import com.example.meurebanho.model.Animal
import com.example.meurebanho.model.PesoAnimal
import com.example.meurebanho.view.ConsultarAnimaisActivity
import com.example.meurebanho.view.ListPesosAnimais
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.Date
import java.util.ArrayList

class pesoDAO ():pesoDAOinterface{

        private lateinit var db: FirebaseFirestore;
        lateinit var lista: ArrayList<PesoAnimal>

        companion object {
            private lateinit var mainActivity: ListPesosAnimais
            private var pesosDAOFirebase: pesoDAO? = null;

            fun getInstance(MainActivity: ListPesosAnimais):pesoDAOinterface{
                if( pesosDAOFirebase == null ){
                    pesosDAOFirebase = pesoDAO( MainActivity );
                }
                return pesosDAOFirebase as pesoDAO;
            }
        }

        private constructor(mainactivity: ListPesosAnimais) : this() {
            mainActivity = mainactivity;
            lista = ArrayList();
        }
        override fun addPeso(a: PesoAnimal):Boolean {
            //mainActivity.controlProgressBar( true );
            val user = hashMapOf<String,Any>()
            user.put("data peso", a.data_peso);
            user.put("peso",a.peso)
            user.put("codigo_animal",a.codigo_animal)
            user.put("animal_id",a.document_id_animal)

            // Add a new document with a generated ID
            //var documento:DocumentReference
            db.collection("Pesos Animais")
                .add( user )
                .addOnSuccessListener{
                    //Toast.makeText( mainActivity, animal.codigo , Toast.LENGTH_LONG ).show();
                    a.document_id_peso=it.id
                    lista.add( a );
                    /* criando nó no Firebase Realtime Database */
                    mainActivity.notifyAdapterpesos();
                    Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();
                }
                .addOnFailureListener {
                    Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();
                };

            //mainActivity.controlProgressBar( false );
            return true;

        }

    override fun editPeso(a: PesoAnimal): Boolean {
            var newAnimal: DocumentReference = db.collection("Pesos Animais").document( a.document_id_peso );
            newAnimal.update("peso", a.peso,
                "datapeso",a.data_peso)
                .addOnSuccessListener{
                    Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();

                    for( animal: PesoAnimal in lista ){

                        if(animal.document_id_animal == a.document_id_animal){
                            animal.peso= a.peso;
                            animal.data_peso=a.data_peso

                            break;

                        }
                    }
                   mainActivity.notifyAdapterpesos();
                }
                .addOnFailureListener{
                        Toast.makeText( mainActivity, "Error", Toast.LENGTH_LONG ).show();
                        //mainActivity.notifyAdapter();
                };

            return true;
        }

        override fun deletePeso(a: String):Boolean{
            var c: PesoAnimal? = null;

            for( animal: PesoAnimal in lista ){
                if( animal.document_id_peso == a) {
                    c = animal;
                    break;
                }
            }

            if( c != null ){

                val apagar: PesoAnimal = c;

                var deleteanimal: DocumentReference = db.collection("Pesos Animais").document( c.document_id_peso );
                deleteanimal.delete().addOnSuccessListener( OnSuccessListener<Void>() {
                    Toast.makeText( mainActivity, "Sucess", Toast.LENGTH_LONG ).show();
                    var animalapagar: PesoAnimal? = null;
                    for( animal: PesoAnimal in lista ){
                        if( animal.document_id_peso== apagar.document_id_peso){
                            animalapagar = animal;
                        }
                        if( animalapagar != null ) {
                            lista.remove( animalapagar );
                           mainActivity.notifyAdapterpesos();
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

        override fun getPeso(codigo: String) :PesoAnimal? {
            return null;
        }

        override fun getListaPesos(document_id:String): ArrayList<PesoAnimal> {
            //mainActivity.controlProgressBar( true );
            db.collection("Pesos Animais")
                .whereEqualTo("animal_id", document_id)
                .orderBy("data peso",com.google.firebase.firestore.Query.Direction.ASCENDING).limit(50)
                .get()
                .addOnCompleteListener( OnCompleteListener {
                    if(it.isSuccessful) {
//                        Toast.makeText(mainActivity, "dados capturados", Toast.LENGTH_SHORT).show();
                        for (data in it.result) {
                            val datapeso= data.getTimestamp("data peso")!!.toDate();
                            val animal_id:String =data.getString("animal_id").toString();
                            val peso_id:String =data.id.toString()
                            val codigo_animal:String =data.getString("codigo_animal").toString();
                            val peso = data.getDouble("peso");

                            val c: PesoAnimal = PesoAnimal(
                                codigo_animal,
                                peso!!.toFloat(),
                                datapeso,
                                animal_id,
                                peso_id
                            );
                            //c.codigo = data.id;

                            lista.add(c);
                        }
                    } else {
                        Log.e("TAG", "erro ao receber.");
                    }
                    mainActivity.notifyAdapterpesos()
                }).addOnFailureListener{
                    Log.e( "Erro FS",it.toString());
                }
            return lista
        }

        override fun init(): Boolean {
            db = FirebaseFirestore.getInstance();
            return true;
        }

        override fun close(): Boolean=false;
        override fun isStarted(): Boolean = false;

    }