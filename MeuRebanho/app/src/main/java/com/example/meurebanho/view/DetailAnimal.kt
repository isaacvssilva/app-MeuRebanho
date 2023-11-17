package com.example.meurebanho.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
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

class DetailAnimal : AppCompatActivity() {
    private lateinit var lineChart: LineChart
    private var xValues = ArrayList<String>()
    private lateinit var documentid:String;
    private lateinit var codigoanimal:String;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_animal)
        initToolbal()


        var datanasc = findViewById<TextView>(R.id.datanasc_animal)
        var especie = findViewById<TextView>(R.id.especie_animal)
        var raca = findViewById<TextView>(R.id.raca_animal)
        var cor = findViewById<TextView>(R.id.cor_animal)
        var codigo = findViewById<TextView>(R.id.codigo_animal)
        var sexo = findViewById<TextView>(R.id.sexo_animal)
        var btn_localizar = findViewById<Button>(R.id.localizar_ani)


        if (intent.extras != null) {
            datanasc.setText("Data de nascimento: "+intent.extras!!.getString(codes.Codes.CHAVE_DATANASC))
            especie.setText("Especie: "+intent.extras!!.getString(codes.Codes.CHAVE_ESPECIE))
            raca.setText ("Raça: "+intent.extras!!.getString(codes.Codes.CHAVE_RACA))
            cor.setText ("Cor: "+intent.extras!!.getString(codes.Codes.CHAVE_COR))
            documentid= (intent.extras!!.getString(codes.Codes.CHAVE_DOCID).toString())
            codigo.setText("N° "+intent.extras!!.getString(codes.Codes.CHAVE_CODIGO))
            codigoanimal=intent.extras!!.getString(codes.Codes.CHAVE_CODIGO).toString()
            sexo.setText("Sexo: "+intent.extras!!.getString(codes.Codes.CHAVE_SEXO))

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

         lineChart=findViewById(R.id.chart)

        var description= Description()
        description.text="Studets"
        description.setPosition(150f,15f)
        lineChart.description=description
        lineChart.axisRight.setDrawLabels(false)

        xValues.addAll(listOf("nadum","kamal","Jerry"))

        var xAxis: XAxis = lineChart.xAxis
        xAxis.position=(XAxis.XAxisPosition.BOTTOM)
        xAxis.valueFormatter= IndexAxisValueFormatter(xValues)
        xAxis.setLabelCount(4)
        xAxis.granularity=(1f)

        var yAxis: YAxis = lineChart.axisLeft
        yAxis.axisMaximum=100f
        yAxis.axisMinimum=0f
        yAxis.axisLineWidth=2f
        yAxis.axisLineColor= Color.BLACK
        yAxis.labelCount=(10)

        var entries1=ArrayList<Entry>()
        entries1.add(Entry(0f,10f))
        entries1.add(Entry(1f,10f))
        entries1.add(Entry(2f,15f))
        entries1.add(Entry(3f,45f))

        var entries2=ArrayList<Entry>()
        entries2.add(Entry(0f,5f))
        entries2.add(Entry(1f,15f))
        entries2.add(Entry(2f,25f))
        entries2.add(Entry(3f,30f))

        var dataSet1= LineDataSet(entries1,"Maths")
        dataSet1.color= Color.BLUE

        var dataSet2=LineDataSet(entries2,"Science")
        dataSet2.color=Color.RED

        var lineData= LineData(dataSet1,dataSet2)

        lineChart.data=lineData

        lineChart.invalidate()

    }
    private fun initToolbal() {
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle("")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater;
        inflater.inflate(R.menu.menu_consultar_animais, menu)
        return super.onCreateOptionsMenu(menu)
    }
}