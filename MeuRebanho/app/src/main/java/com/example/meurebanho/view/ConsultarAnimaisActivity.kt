package com.example.meurebanho.view
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meurebanho.R
import com.example.meurebanho.model.Animal
import com.example.meurebanho.rebanhoDAO.rebanhoDAO
import com.example.meurebanho.rebanhoDAO.rebanhoDAOinterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.ArrayList
import java.util.Collections

class ConsultarAnimaisActivity:AppCompatActivity(){

    private lateinit var animalDAO: rebanhoDAOinterface
    companion object{
        var animais= ArrayList<Animal>()
        private lateinit var adapterani:AnimalAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_consultar_animais )
        val recyclerView=findViewById<RecyclerView>(R.id.recycler_view)
        initToolbal()

//        if (savedInstanceState == null) {
//            supportFragmentManager
//                .beginTransaction()
//                .add(
//                    R.id.container_root, fragmentlist.newInstance(),
//                    "fragmentlist"
//                )
//                .commit()
        animalDAO= rebanhoDAO.getInstance(this)

        animalDAO.init()

        if(!animais.isEmpty())
            animais.clear()

        animais = animalDAO.getListaanimais()

        adapterani=AnimalAdapter(this,animais)
        recyclerView.adapter= adapterani
        recyclerView.layoutManager=LinearLayoutManager(this)

        val helper=androidx.recyclerview.widget.ItemTouchHelper(
            ItemTouchHelper(
                androidx.recyclerview.widget.ItemTouchHelper.UP
                    or androidx.recyclerview.widget.ItemTouchHelper.DOWN,
                0)
        )
        helper.attachToRecyclerView(recyclerView)


            var buttonadd =findViewById<FloatingActionButton>(R.id.add_animal)

            buttonadd.setOnClickListener {
                val intent = Intent(this, CadastroAnimalActivity::class.java)
                startActivity(intent)
            }


        //listView.setSelector(android.R.color.holo_blue_dark);

    }
    inner class ItemTouchHelper(dragDirs:Int,swipeDirs:Int):androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
        dragDirs, swipeDirs
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from:Int=viewHolder.adapterPosition
            val to:Int= target.adapterPosition
            Collections.swap(adapterani.animais,from,to)
            adapterani.notifyItemMoved(from,to)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapterani.animais.removeAt(viewHolder.adapterPosition)
            adapterani.notifyItemRemoved(viewHolder.adapterPosition)
        }
    }

    private fun initToolbal(){
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
    fun notifyAdapter() {
        Log.d("tamlist", adapterani.itemCount.toString())
        adapterani.notifyDataSetChanged()
    }

    fun confirm_remove_Animal(A:Animal){
        openDialog(A)
    }

    fun remove_animal(A:Animal){
        animalDAO.deleteAnimal(A.codigo)
    }

    fun openDialog(A:Animal){
        var dialog: Dialog = Dialog(this)
        var layoutInflater: LayoutInflater =this.layoutInflater
        val customdialog:View=layoutInflater.inflate(R.layout.caixa_confirmacao, null)
        dialog.setContentView(customdialog)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val button=customdialog.findViewById<Button>(R.id.btn_confirmar)
        button.setOnClickListener{
            remove_animal(A)
            dialog.dismiss()
        }
        val buttoncancelar=customdialog.findViewById<Button>(R.id.btn_cancelar)
        buttoncancelar.setOnClickListener{
            dialog.cancel()
        }
        dialog.show()
    }
//    override fun onSelected(character: Animal) {
//    val args= Bundle()
//    args.putSerializable("detail",character)
//    val fragment =fragmentMain.newInstance()
//    fragment.arguments= args
//    supportFragmentManager
//        .beginTransaction()
//        .replace(R.id.container_root,fragment,"fragmentDetail")
//        .addToBackStack(null)
//        .commit()
//}
}