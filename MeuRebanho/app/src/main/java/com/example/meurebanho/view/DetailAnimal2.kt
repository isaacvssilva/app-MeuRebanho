package com.example.meurebanho.view

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.meurebanho.R
import com.example.meurebanho.databinding.ActivityDetailAnimalBinding

class DetailAnimal2: AppCompatActivity() {

    //private lateinit var lineChart: LineChart
    //private var xValues = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_detail_animal)
        initToolbal()
        Toast.makeText(this, "sexo:", Toast.LENGTH_SHORT).show()


       // lineChart=findViewById(R.id.chart)

//        var description= Description()
//        description.text="Studets"
//        description.setPosition(150f,15f)
//        lineChart.description=description
//        lineChart.axisRight.setDrawLabels(false)
//
//        xValues.addAll(listOf("nadum","kamal","Jerry"))
//
//        var xAxis:XAxis= lineChart.xAxis
//        xAxis.position=(XAxis.XAxisPosition.BOTTOM)
//        xAxis.valueFormatter= IndexAxisValueFormatter(xValues)
//        xAxis.setLabelCount(4)
//        xAxis.granularity=(1f)
//
//        var yAxis:YAxis= lineChart.axisLeft
//        yAxis.axisMaximum=100f
//        yAxis.axisMinimum=0f
//        yAxis.axisLineWidth=2f
//        yAxis.axisLineColor= Color.BLACK
//        yAxis.labelCount=(10)
//
//        var entries1=ArrayList<Entry>()
//        entries1.add(Entry(0f,10f))
//        entries1.add(Entry(1f,10f))
//        entries1.add(Entry(2f,15f))
//        entries1.add(Entry(3f,45f))
//
//        var entries2=ArrayList<Entry>()
//        entries2.add(Entry(0f,5f))
//        entries2.add(Entry(1f,15f))
//        entries2.add(Entry(2f,25f))
//        entries2.add(Entry(3f,30f))
//
//        var dataSet1=LineDataSet(entries1,"Maths")
//        dataSet1.color=Color.BLUE
//
//        var dataSet2=LineDataSet(entries2,"Science")
//        dataSet2.color=Color.RED
//
//        var lineData=LineData(dataSet1,dataSet2)
//
//        lineChart.data=lineData
//
//        lineChart.invalidate()

    }

    private fun initToolbal() {
        var toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater;
        inflater.inflate(R.menu.menu_consultar_animais, menu)
        return super.onCreateOptionsMenu(menu)
    }


}