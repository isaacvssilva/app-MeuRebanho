package com.example.meurebanho.view
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.meurebanho.R
import com.example.meurebanho.model.Animal
import com.example.meurebanho.rebanhoDAO.rebanhoDAO
import com.example.meurebanho.rebanhoDAO.rebanhoDAOinterface

class ConsultarAnimaisActivity:AppCompatActivity(),fragmentlist.OnlistSelected {

    //var selected:Int=0;
//    companion object{
//        var listaAnimais=ArrayList<Animal>()
//        private var listespAnimais=ArrayList<String>()
//        lateinit var adapterAnimais:ArrayAdapter<String>;
//    }

    //private lateinit var animalDAO: rebanhoDAOinterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_consultar_animais )
        initToolbal()
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.container_root, fragmentlist.newInstance(),
                    "fragmentlist"
                )
                .commit()
        }


        //listView.setSelector(android.R.color.holo_blue_dark);

    }
    fun initToolbal(){
        var toolbar: Toolbar =findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if(supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater:MenuInflater= menuInflater;
        inflater.inflate(R.menu.menu_consultar_animais,menu)
        return super.onCreateOptionsMenu(menu)
    }
//    fun notifyAdapter() {
//        listespAnimais.clear()
//        for(it in listaAnimais){
//            //Toast.makeText(this,it.codigo, Toast.LENGTH_SHORT ).show();
//            listespAnimais.add(it.codigo)
//        }
//        adapterAnimais.notifyDataSetChanged()
//    }
    override fun onSelected(character: Animal) {
    val args= Bundle()
    args.putSerializable("detail",character)
    val fragment =fragmentMain.newInstance()
    fragment.arguments= args
    supportFragmentManager
        .beginTransaction()
        .replace(R.id.container_root,fragment,"fragmentDetail")
        .addToBackStack(null)
        .commit()
}
}