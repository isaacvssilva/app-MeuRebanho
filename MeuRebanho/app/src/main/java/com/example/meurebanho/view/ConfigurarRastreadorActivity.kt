package com.example.meurebanho.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.meurebanho.R
import com.example.meurebanho.databinding.ActivityConfigurarRastreadorBinding
import com.example.meurebanho.databinding.ActivityLocalizarAnimalBinding
import com.example.meurebanho.model.Animal
import com.example.meurebanho.model.Character
import com.example.meurebanho.rebanhoDAO.rebanhoDAO
import com.example.meurebanho.rebanhoDAO.rebanhoDAOinterface

class ConfigurarRastreadorActivity : AppCompatActivity(),fragmentlist.OnlistSelected{


//    lateinit var fragmentMagager: FragmentManager;
//    lateinit var FrangmentMain:fragmentMain;
//        lateinit var animalDAO: rebanhoDAOinterface
//3

    private val binding by lazy {
        ActivityConfigurarRastreadorBinding.inflate( layoutInflater )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
//        animalDAO.
//        animalDAO.init()
    inicializaToolbar();
    if (savedInstanceState == null) {
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.container_root, fragmentlist.newInstance(),
                "fragmentlist"
            )
            .commit()
    }


//        FrangmentMain = fragmentMain();
//
//        fragmentMagager= fragmentMagager;R.drawable.graficos
//        var transition: FragmentTransaction? = fragmentMagager.beginTransaction();fragmentMagager
//        transition!!.add(R.id.fragment_main , FrangmentMain)
//        transition.commit();
}
    private fun inicializaToolbar() {
        val toolbar = binding.tbConfigRastreador.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Configurar rastreador"
            setDisplayHomeAsUpEnabled(true)
        }
    }

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

//    fun showMessage(view: View):Unit{
//        var main:fragmentMain = fragmentMagager.findFragmentById(R.id.fragment_main) as fragmentMain
//        main.showMessage("Acessando o fragment")
//    }


