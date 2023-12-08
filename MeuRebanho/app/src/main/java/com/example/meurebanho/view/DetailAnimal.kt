package com.example.meurebanho.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.meurebanho.NetworkUtils
import com.example.meurebanho.R
import com.example.meurebanho.controller.codes
import com.example.meurebanho.model.PesoAnimal
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class DetailAnimal : AppCompatActivity() {
    private lateinit var lineChart: LineChart
    private var xValues = ArrayList<String>()
    private var arraypesos = ArrayList<PesoAnimal>()
    val formatDAte = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    companion object {
        private lateinit var datanasc: String
        private lateinit var especie: String
        private lateinit var raca: String
        private lateinit var cor: String
        private lateinit var codigo: String
        private lateinit var sexo: String
        private lateinit var documentid: String;
        private lateinit var codigoanimal: String;
    }

    private val firestore by lazy {
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
        initToolbal()

        /* Inicializando o cliente de provedor de localizacao fundida */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        var inputdatanasc = findViewById<TextView>(R.id.datanasc_animal)
        var inputespecie = findViewById<TextView>(R.id.especie_animal)
        var inputraca = findViewById<TextView>(R.id.raca_animal)
        var inputcor = findViewById<TextView>(R.id.cor_animal)
        var inputcodigo = findViewById<TextView>(R.id.codigo_animal)
        var inputsexo = findViewById<TextView>(R.id.sexo_animal)
        var btn_localizar = findViewById<Button>(R.id.localizar_ani)
        var btn_historico = findViewById<Button>(R.id.btn_historico_peso)


        if (intent.extras != null) {
            datanasc =
                ("Data de nascimento: " + intent.extras!!.getString(codes.Codes.CHAVE_DATANASC))
            especie = ("Especie: " + intent.extras!!.getString(codes.Codes.CHAVE_ESPECIE))
            raca = ("Raça: " + intent.extras!!.getString(codes.Codes.CHAVE_RACA))
            cor = ("Cor: " + intent.extras!!.getString(codes.Codes.CHAVE_COR))
            documentid = intent.extras!!.getString(codes.Codes.CHAVE_DOCID).toString()
            codigo = ("N° " + intent.extras!!.getString(codes.Codes.CHAVE_CODIGO))
            codigoanimal = intent.extras!!.getString(codes.Codes.CHAVE_CODIGO).toString()
            sexo = ("Sexo: " + intent.extras!!.getString(codes.Codes.CHAVE_SEXO))

        }

        locAnimalCloud(codigoanimal)

        inputdatanasc.setText(datanasc)
        inputespecie.setText(especie)
        inputraca.setText(raca)
        inputcor.setText(cor)
        inputcodigo.setText(codigo)
        inputsexo.setText(sexo)

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

        btn_historico.setOnClickListener {
            if (NetworkUtils.isInternetAvailable(applicationContext)) {
                val intent = Intent(this, ListPesosAnimais::class.java)
                intent.putExtra(codes.Codes.CHAVE_DOCID, documentid)
                intent.putExtra(codes.Codes.CHAVE_CODIGO, codigoanimal)
                startActivity(intent)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Sem conexão à Internet. Tente novamente mais tarde.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        if (arraypesos.isEmpty()) {
            getListaPesos()
        }

    }

    private fun initToolbal() {
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setTitle("")
    }

    private fun getListaPesos():Boolean {
        Log.d("TAG", "dados recebidos");
        firestore.collection("Pesos Animais")
            .whereEqualTo("animal_id", documentid)
            .orderBy("data peso", com.google.firebase.firestore.Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener(OnCompleteListener{
                if(it.isSuccessful){
                for (data in it.result) {
                    val datapeso = data.getTimestamp("data peso")!!.toDate();
                    val animal_id: String = data.getString("animal_id").toString();
                    val codigo_animal: String = data.getString("codigo_animal").toString();
                    val peso = data.getDouble("peso");

                    val c: PesoAnimal = PesoAnimal(
                        codigo_animal,
                        peso!!.toFloat(),
                        datapeso,
                        animal_id,
                        data.id
                    )
                    arraypesos.add(c)
                }
                    fazergrafico()
                }else {
                    Log.e("TAG", "erro ao receber.");
                }
            }).addOnFailureListener {
                Log.e("Erro FS", it.toString());
            }
        return true
    }

    fun fazergrafico(){
        lineChart = findViewById(R.id.chart)

        var description = Description()
        description.text = "Peso"
        description.setPosition(1100f, 15f)
        lineChart.description = description
        lineChart.axisRight.setDrawLabels(false)


        var entries1 = ArrayList<Entry>()
        for(( i , data) in arraypesos.withIndex()) {
            xValues.add(formatDAte.format(data.data_peso))
            entries1.add(Entry(i.toFloat(), data.peso))
        }


        var xAxis: XAxis = lineChart.xAxis
        xAxis.position = (XAxis.XAxisPosition.BOTTOM)
        xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.axisLineWidth=3f
        xAxis.setLabelCount(arraypesos.size)
        xAxis.granularity = (1f)

        var yAxis: YAxis = lineChart.axisLeft
        yAxis.axisMaximum = 1000f
        yAxis.axisMinimum = 0f
        yAxis.axisLineWidth = 3f
        yAxis.axisLineColor = Color.BLACK
        yAxis.labelCount = (50)

        var dataSet1 = LineDataSet(entries1, "Peso")
        dataSet1.color = Color.GREEN
        dataSet1.circleRadius=3f
        dataSet1.lineWidth=4f

        var lineData = LineData(dataSet1)

        lineChart.data = lineData

        lineChart.invalidate()
    }

    /* Função que consulta os dados de um animal no banco de dados com base em seu ID.
   Ela realiza uma consulta ao banco de dados, atualiza o modelo do animal, calcula a
   distância entre o usuário e o animal e atualiza a interface do usuário. A função também
   mantém a distância atualizada em tempo real durante a consulta ativa utilizando threads. */
    private fun locAnimalCloud(IdAnimal: String) {
        val refAnimal = firestore.collection("Animais").document(IdAnimal)

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
                            val distanciaFormatada = String.format("Distância: %.2f km", distanciaKm)

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
