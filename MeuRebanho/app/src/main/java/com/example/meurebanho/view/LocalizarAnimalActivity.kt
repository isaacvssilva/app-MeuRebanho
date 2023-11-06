package com.example.meurebanho.view

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.meurebanho.NetworkUtils
import com.example.meurebanho.R
import com.example.meurebanho.databinding.ActivityLocalizarAnimalBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class LocalizarAnimalActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLocalizarAnimalBinding.inflate(layoutInflater)
    }

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var recycleViewLocAnimal: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var locAnimalAdapter: LocalizaAnimalResultAdapter
    private lateinit var locAnimalList: ArrayList<LocalizaAnimalResultModel>

    /* coordenadas padroes (centro do mapa) */
    private var origem = LatLng(0.0, 0.0)
    private var destino = LatLng(0.0, 0.0)

    private var posAtualLat: Double = 0.0
    private var posAtualLon: Double = 0.0

    /*flag para realizar o controle do processo de thread */
    private var atualizacaoContinua = true
    private val coordenadasAnimais = mutableMapOf<String, Pair<Double, Double>>()


    /* Referencia ao cliente de provedor de localização fundida
  * usado para acessar a localizacao do dispositivo */
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        /* Inicializando o cliente de provedor de localizacao fundida */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        inicializaToolbar()
        initializeRecyclerView()
        initializeSearchView()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbLocalizaAnimal.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Localizar animal"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun onAnimalItemClick(animal: LocalizaAnimalResultModel) {
        val intent = Intent(this, LocalizaMapsActivity::class.java)
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()

        /* Obtendo o id do animal informado pelo usuario */
        val animalCode = searchView.query.toString()

        /* Carregando dado para a activity do google maps */
        intent.putExtra("id", animalCode)
        startActivity(intent)
    }

    private fun initializeRecyclerView() {
        recycleViewLocAnimal = findViewById(R.id.LocRecyclerView)
        locAnimalList = ArrayList()
        locAnimalAdapter = LocalizaAnimalResultAdapter(locAnimalList, ::onAnimalItemClick)
        recycleViewLocAnimal.adapter = locAnimalAdapter
    }

    private fun initializeSearchView() {
        searchView = findViewById(R.id.idSV)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                /* Verificando se há conexao com a internet */
                if (NetworkUtils.isInternetAvailable(applicationContext)) {
                    // Chamada para realizar a consulta ao Firestore com o ID informado
                    locListaAnimalCloud(query)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Sem conexão à Internet. Tente novamente mais tarde.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    /* Função que consulta os dados de um animal no banco de dados com base em seu ID.
    Ela realiza uma consulta ao banco de dados, atualiza o modelo do animal, calcula a
    distância entre o usuário e o animal e atualiza a interface do usuário. A função também
    mantém a distância atualizada em tempo real durante a consulta ativa utilizando threads. */
    private fun locListaAnimalCloud(IdAnimal: String) {
        val refAnimal = bancoDados.collection("Animais").document(IdAnimal)

        refAnimal.addSnapshotListener { valor, erro ->
            val dadosLocAnimal = valor?.data
            if (dadosLocAnimal != null) {
                val racaAnimal = dadosLocAnimal["raca"]
                val animalModel =
                    LocalizaAnimalResultModel("", racaAnimal.toString(), "Calculando...")

                /* Limpando a lista de animais e adinicionando novo modelo */
                locAnimalList.clear()
                locAnimalList.add(animalModel)

                val distanciaPos = FloatArray(1)

                /* Thread para calcular a distancia entre
                 o usuario e o animal, para exibir no cardview */
                val CalculaDistanciaAnimal = Thread {

                    /* flag true para obter a atulizacao da posicao*/
                    while (atualizacaoContinua) {
                        /* capturando a posicao do usuario */
                        posicaoAtualUsuario()

                        /* capturando a posicao do animal */
                        posicaoAtualAnimal(IdAnimal)

                        /* Obtendo as coordenadas do animal a partir da lista de coordenadasAnimais */
                        val coordenadas = coordenadasAnimais[IdAnimal]


                        /* Verificando se as coordenadas do animal existem  */
                        if (coordenadas != null) {
                            val latitudeAnimal = coordenadas.first
                            val longitudeAnimal = coordenadas.second

                            /* Calculando a distância entre a posição atual e a posição do animal */
                            Location.distanceBetween(
                                posAtualLat, posAtualLon,
                                latitudeAnimal, longitudeAnimal, distanciaPos
                            )
                        }

                        /* Convertendo a distância de metros para quilômetros  */
                        val distanciaKm = distanciaPos[0] / 1000

                        /* Formatando a distância com duas casas decimais e a unidade "km" */
                        val distanciaFormatada = String.format("%.2f km", distanciaKm)

                        /* Atualizando o modelo do animal com a distância calculada */
                        animalModel.distancia = distanciaFormatada

                        /* Notificando o adaptador para atualizar a interface do usuário na thread principal */
                        runOnUiThread {
                            locAnimalAdapter?.notifyDataSetChanged()
                        }

                        /* Aguarda 2 segundos antes da próxima atualização */
                        Thread.sleep(2000)
                    }
                }
                CalculaDistanciaAnimal.start()
            }
        }
    }


    /* Funcao que coleta a posicao atual do usuario consultando o GPS */
    private fun posicaoAtualUsuario() {

        /* verificando permissoes de localizacao */
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return
        }

        /* Pegando as coordenadas da posicao atual do usuario */
        val localizacao = fusedLocationClient.lastLocation
        localizacao.addOnSuccessListener {
            if (it != null) {
                origem = LatLng(it.latitude, it.longitude)
                posAtualLat = origem.latitude
                posAtualLon = origem.longitude
            }

        }
    }

    /* Funcao que coleta as coordenadas da posicao do animal consultando o Firebase */
    private fun posicaoAtualAnimal(animalId: String) {
        /* Obtendo referencia ao no "Animal" via Firebase Realtime Database */
        val database = FirebaseDatabase.getInstance().getReference("Animal/$animalId")

        /* Obtendo valor do no "GPS" via Firebase Realtime Database */
        database.child("GPS").get().addOnSuccessListener { gpsSnapshot ->
            if (gpsSnapshot.exists()) {
                /* Pegando a string referente as coordenadas da posicao atual do animal */
                val gps: String = gpsSnapshot.value.toString()

                /* Dividindo a string nas coordenadas de latitude e longitude */
                val coordenadas = gps.split(",")

                if (coordenadas.size == 2) {
                    val latitude = coordenadas[0].toDoubleOrNull()
                    val longitude = coordenadas[1].toDoubleOrNull()

                    if (latitude != null && longitude != null) {

                        /* Atualizando a posição de destino */
                        destino = LatLng(latitude, longitude)
                        coordenadasAnimais[animalId] = Pair(destino.latitude, destino.longitude)

                    } else {
                        Toast.makeText(this, "Coordenadas inválidas", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Formato de coordenadas inválido", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(this, "Path Animal/$animalId/GPS não existe", Toast.LENGTH_LONG)
                    .show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "FAILED", Toast.LENGTH_LONG).show()
        }
    }

    /* Sobrescrevendo o metodo onDestroy para encerrar a atualizacao quando
     a activity estiver sendo destruida */
    override fun onDestroy() {
        super.onDestroy()
        /* Sinalizando a parada da atualizacao  */
        atualizacaoContinua = false
    }
}