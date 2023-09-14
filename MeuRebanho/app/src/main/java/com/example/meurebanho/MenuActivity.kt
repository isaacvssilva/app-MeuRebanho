package com.example.meurebanho

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MenuActivity : AppCompatActivity() {

    /* Declaracao de variaveis */
    private var recyclerViewMenu: RecyclerView? = null
    private var recyclerViewMenuAdapter: MenuItemAdapter? = null
    private var menuList = mutableListOf<MenuData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        /* Inicializando a lista do Menu principal */
        menuList = ArrayList()

        /* Associando elemento RecyclerView  do XML com a variavel na classe para manipulacao */
        recyclerViewMenu = findViewById<View>(R.id.rv_list_menu) as RecyclerView

        recyclerViewMenuAdapter = MenuItemAdapter(this, menuList) { menuData ->
            /* Acoes de clique nos itens do menu: abrindo uma nova activity correspondente */
            when (menuData.titulo) {
                "Cadastrar animal" -> {
                    val intent = Intent(this, CadastroAnimalActivity::class.java)
                    startActivity(intent)
                }
                "Localizar animal" -> {
                    val intent = Intent(this, LocalizarAnimalActivity::class.java)
                    startActivity(intent)
                }
                "Consultar animais" -> {
                    val intent = Intent(this, ConsultarAnimaisActivity::class.java)
                    startActivity(intent)
                }
                "Relatórios" -> {
                    val intent = Intent(this, RelatoriosActivity::class.java)
                    startActivity(intent)
                }
                "Alertas" -> {
                    val intent = Intent(this, AlertasActivity::class.java)
                    startActivity(intent)
                }
                "Config. rastreador" -> {
                    val intent = Intent(this, ConfigurarRastreadorActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                }
            }
        }
        /* Configurando o layout da RecyclerView como um GridLayoutManager com 2 colunas */
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
        recyclerViewMenu!!.layoutManager = layoutManager

        /* Configurando o adaptador para a RecyclerView */
        recyclerViewMenu!!.adapter = recyclerViewMenuAdapter

        /* Chamando a funcao para preparar os dados da lista do Menu*/
        prepareMenuListData()
    }

    /* Preparando os dados da lista do Menu e adicionando os itens */
    private fun prepareMenuListData() {
        /* Criando um objeto MenuData para "Cadastrar animal" com a imagem correspondente */
        var menu = MenuData("Cadastrar animal", R.drawable.add_animal)
        menuList.add(menu)

        /* Criando um objeto MenuData para "Localizar animal" com a imagem correspondente */
        menu = MenuData("Localizar animal", R.drawable.rota_loc)
        menuList.add(menu)

        /* Criando um objeto MenuData para "Consultar animais" com a imagem correspondente */
        menu = MenuData("Consultar animais", R.drawable.search)
        menuList.add(menu)

        /* Criando um objeto MenuData para "Relatórios" com a imagem correspondente */
        menu = MenuData("Relatórios", R.drawable.graficos)
        menuList.add(menu)

        /* Criando um objeto MenuData para "Alertas" com a imagem correspondente */
        menu = MenuData("Alertas", R.drawable.alertas)
        menuList.add(menu)

        /* Criando um objeto MenuData para "Configurar rastreador" com a imagem correspondente */
        menu = MenuData("Config. rastreador", R.drawable.loc_config)
        menuList.add(menu)

        /* Notificando o adaptador de que os dados estão sendo atualizados */
        recyclerViewMenuAdapter?.notifyDataSetChanged()

    }
}