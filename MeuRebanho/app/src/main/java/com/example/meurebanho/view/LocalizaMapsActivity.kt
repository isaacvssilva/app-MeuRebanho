package com.example.meurebanho.view

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.meurebanho.R
import com.example.meurebanho.databinding.ActivityLocalizaMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase

class LocalizaMapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocalizaMapsBinding

    /* coordenadas padroes (centro do mapa) */
    private var origem = LatLng(0.0, 0.0)
    private var destino: LatLng = LatLng(0.0, 0.0)
    private var googleMap: GoogleMap? = null

    private var posAtualLat: Double = 0.0
    private var posAtualLon: Double = 0.0
    private var atualizacaoContinua = false

    /* Referencia ao cliente de provedor de localização fundida
  * usado para acessar a localizacao do dispositivo */
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocalizaMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*  Obtendo referencia ao fragmento do mapa definido no layout */
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { mMap ->
            googleMap = mMap
        }
        /* Inicializando o cliente de provedor de localizacao fundida */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        /* Inicializando ToolBar para a interface de maps */
        inicializaToolbar()
        mapsConfig()
        atualizaPosicao()
    }

    private fun inicializaToolbar() {
        val toolbar = binding.tbMaps.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Geolocalização"
            setDisplayHomeAsUpEnabled(true)
        }
    }


    private fun mapsConfig() {

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
        val localizacao = fusedLocationClient.lastLocation
        localizacao.addOnSuccessListener {
            if (it != null) {
                googleMap?.uiSettings?.isCompassEnabled = true
                googleMap?.uiSettings?.isZoomControlsEnabled = true
                googleMap?.isMyLocationEnabled = true
                googleMap?.mapType = GoogleMap.MAP_TYPE_HYBRID
            }
        }
    }

    private fun atualizaPosicao() {
        atualizacaoContinua = true
        val threadAtualizacao = Thread {
            while (atualizacaoContinua) {
                runOnUiThread {
                    /* Inicializando funcao que captura a posicao do usuario */
                    posicaoAtualUsuario()

                    /* recebendo dados do search view com o id do animal */
                    val bundle = intent.extras
                    if (bundle != null) {
                        val id = bundle.getString("id")
                        /* Inicializando funcao que captura a posicao do animal */
                        posicaoAtualAnimal(id.toString())
                    }
                    /* Limpando o mapa para evitar acumulacao de marcadores antigos */
                    googleMap?.clear()
                }
                Thread.sleep(1000)
            }
        }
        threadAtualizacao.start()
    }

    private var marcadorOrigem: Marker? = null // Referencia ao marcador de origem
    private var marcadorDestino: Marker? = null // Referencia ao marcador de destino

    /* Funcao que adiciona marcador no ponto de origem e de destino */
    private fun addMarkers(googleMap: GoogleMap) {
        /* Adicionando marcador de origem apos a leitura da posicao atual */
        if (origem.latitude != 0.0 && origem.longitude != 0.0) {

            /* Criando marcador para o origem */
            marcadorOrigem = googleMap.addMarker(
                MarkerOptions()
                    /* Definindo a posicao do marcador via coordenadas */
                    .position(origem)
                    .title("Sua Localização")
            )
        }

        /* Removendo marcador de destino, caso exista no mapa */
        marcadorDestino?.remove()

        /* Adicionando marcador de destino apos a leitura dos dados do Firebase */
        if (destino.latitude != 0.0 && destino.longitude != 0.0) {

            /* Criando marcador para o destino */
            marcadorDestino = googleMap.addMarker(
                MarkerOptions()
                    /* Definindo a posicao do marcador via coordenadas */
                    .position(destino)
                    .title("Animal")
            )
        }
    }

    /* Funcao que mostra a posicao atual do usuario consultando o GPS */
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
        localizacao.addOnSuccessListener { it ->
            if (it != null) {
                /* Atualizando marcador para a nova posicao */
                origem = LatLng(it.latitude, it.longitude)
                posAtualLat = origem.latitude
                posAtualLon = origem.longitude
                googleMap?.let { addMarkers(it) }
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

                        /* Atualizando a posicao de destino */
                        destino =
                            LatLng(latitude.toString().toDouble(), longitude.toString().toDouble())

                        /* Atualizando marcador para a nova posicao */
                        googleMap?.let { addMarkers(it) }

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

    /* Sobrescrevendo o metodo onDestroy para
    encerrar a atualizacao quando a activity estiver sendo destruida */
    override fun onDestroy() {
        super.onDestroy()
        /* Sinalizando a parada da atualizacao  */
        atualizacaoContinua = false
    }

}