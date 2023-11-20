package com.example.meurebanho.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.meurebanho.NetworkUtils
import com.example.meurebanho.R
import com.example.meurebanho.controller.codes
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class DetailAnimal : AppCompatActivity() {
    private lateinit var lineChart: LineChart
    private var xValues = ArrayList<String>()
    private lateinit var documentid: String;
    private lateinit var codigoanimal: String;

    private val bancoDados by lazy {
        FirebaseFirestore.getInstance()
    }

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
        setContentView(R.layout.activity_detail_animal)
        /* Inicializando o cliente de provedor de localizacao fundida */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        initToolbal()


        var datanasc = findViewById<TextView>(R.id.datanasc_animal)
        var especie = findViewById<TextView>(R.id.especie_animal)
        var raca = findViewById<TextView>(R.id.raca_animal)
        var cor = findViewById<TextView>(R.id.cor_animal)
        var codigo = findViewById<TextView>(R.id.codigo_animal)
        var sexo = findViewById<TextView>(R.id.sexo_animal)
        var btn_localizar = findViewById<Button>(R.id.localizar_ani)
        var btn_add_peso = findViewById<Button>(R.id.btn_add_peso)


        if (intent.extras != null) {
            datanasc.setText("Data de nascimento: " + intent.extras!!.getString(codes.Codes.CHAVE_DATANASC))
            especie.setText("Especie: " + intent.extras!!.getString(codes.Codes.CHAVE_ESPECIE))
            raca.setText("Raça: " + intent.extras!!.getString(codes.Codes.CHAVE_RACA))
            cor.setText("Cor: " + intent.extras!!.getString(codes.Codes.CHAVE_COR))
            documentid = (intent.extras!!.getString(codes.Codes.CHAVE_DOCID).toString())
            codigo.setText("N° " + intent.extras!!.getString(codes.Codes.CHAVE_CODIGO))
            codigoanimal = intent.extras!!.getString(codes.Codes.CHAVE_CODIGO).toString()
            sexo.setText("Sexo: " + intent.extras!!.getString(codes.Codes.CHAVE_SEXO))

            locAnimalCloud(codigoanimal)
        }

        btn_localizar.setOnClickListener {
            val intent = Intent(this, LocalizaMapsActivity::class.java)
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()

            /* Carregando dado para a activity do google maps */
            intent.putExtra("id", codigoanimal)

            /* Verificando se há conexao com a internet */
            if (NetworkUtils.isInternetAvailable(applicationContext)) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Sem conexão à Internet. Tente novamente mais tarde.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btn_add_peso.setOnClickListener {
            val intent = Intent(this, PesoAnimal::class.java)
            if (NetworkUtils.isInternetAvailable(applicationContext)) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Sem conexão à Internet. Tente novamente mais tarde.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        lineChart = findViewById(R.id.chart)

        var description = Description()
        description.text = "Studets"
        description.setPosition(150f, 15f)
        lineChart.description = description
        lineChart.axisRight.setDrawLabels(false)

        xValues.addAll(listOf("nadum", "kamal", "Jerry"))

        var xAxis: XAxis = lineChart.xAxis
        xAxis.position = (XAxis.XAxisPosition.BOTTOM)
        xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.setLabelCount(4)
        xAxis.granularity = (1f)

        var yAxis: YAxis = lineChart.axisLeft
        yAxis.axisMaximum = 100f
        yAxis.axisMinimum = 0f
        yAxis.axisLineWidth = 2f
        yAxis.axisLineColor = Color.BLACK
        yAxis.labelCount = (10)

        var entries1 = ArrayList<Entry>()
        entries1.add(Entry(0f, 10f))
        entries1.add(Entry(1f, 10f))
        entries1.add(Entry(2f, 15f))
        entries1.add(Entry(3f, 45f))

        var entries2 = ArrayList<Entry>()
        entries2.add(Entry(0f, 5f))
        entries2.add(Entry(1f, 15f))
        entries2.add(Entry(2f, 25f))
        entries2.add(Entry(3f, 30f))

        var dataSet1 = LineDataSet(entries1, "Maths")
        dataSet1.color = Color.BLUE

        var dataSet2 = LineDataSet(entries2, "Science")
        dataSet2.color = Color.RED

        var lineData = LineData(dataSet1, dataSet2)

        lineChart.data = lineData

        lineChart.invalidate()

    }

    private fun initToolbal() {
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("")
    }

    /* Função que consulta os dados de um animal no banco de dados com base em seu ID.
    Ela realiza uma consulta ao banco de dados, atualiza o modelo do animal, calcula a
    distância entre o usuário e o animal e atualiza a interface do usuário. A função também
    mantém a distância atualizada em tempo real durante a consulta ativa utilizando threads. */
    private fun locAnimalCloud(IdAnimal: String) {
        val refAnimal = bancoDados.collection("Animais").document(IdAnimal)

        refAnimal.addSnapshotListener { valor, erro ->
            val dadosLocAnimal = valor?.data
            if (dadosLocAnimal != null) {
                val racaAnimal = dadosLocAnimal["raca"]
                val animalModel =
                    LocalizaAnimalResultModel("", racaAnimal.toString(), "Calculando...")

                /* Thread para calcular a distância entre
                 o usuário e o animal, para exibir no cardview */
                val calculaDistanciaAnimal = Thread {

                    /* flag true para obter a atualização da posição */
                    while (atualizacaoContinua) {
                        /* capturando a posição do usuário */
                        posicaoAtualUsuario()

                        /* capturando a posição do animal */
                        posicaoAtualAnimal(IdAnimal)

                        /* Obtendo as coordenadas do animal a partir da lista de coordenadasAnimais */
                        val coordenadas = coordenadasAnimais[IdAnimal]

                        /* Verificando se as coordenadas do animal existem  */
                        if (coordenadas != null) {
                            val latitudeAnimal = coordenadas.first
                            val longitudeAnimal = coordenadas.second

                            /* Calculando a distância entre a posição atual e a posição do animal */
                            val distanciaPos = FloatArray(1)
                            Location.distanceBetween(
                                posAtualLat, posAtualLon,
                                latitudeAnimal, longitudeAnimal, distanciaPos
                            )

                            /* Convertendo a distância de metros para quilômetros  */
                            val distanciaKm = distanciaPos[0] / 1000

                            /* Formatando a distância com duas casas decimais e a unidade "km" */
                            val distanciaFormatada = String.format("%.2f km", distanciaKm)

                            /* Atualizando o modelo do animal com a distância calculada */
                            animalModel.distancia = distanciaFormatada

                            runOnUiThread {
                                /* Atualizando o TextView com a distância calculada */
                                val textViewDistanciaAnimal = findViewById<TextView>(R.id.locDistanciaAnimal)
                                textViewDistanciaAnimal.text = animalModel.distancia
                            }
                        }

                        /* Aguarda 2 segundos antes da próxima atualização */
                        Thread.sleep(2000)
                    }
                }
                calculaDistanciaAnimal.start()
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
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        var inflater: MenuInflater = menuInflater;
//        inflater.inflate(R.menu.menu_consultar_animais, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//}