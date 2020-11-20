package com.example.covidtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_header,list,false))
        fetchResults()
    }
    fun fetchResults()
    {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { client.api.execute() }

            if(response.isSuccessful)
            {
                //Log.i("info", response.body?.string().toString())
                val data= Gson().fromJson(response.body?.string(),Response::class.java)
                launch(Dispatchers.Main) {
                    bindCombinedData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(1,data.statewise.size))
                }
            }


        }//since it is a network call we use withContext(dispatcher.io)

    }

    private fun bindStateWiseData(subList: List<StatewiseItem>) {
        var listAdapter=listadapter(subList)
        list.adapter=listAdapter
    }

    fun bindCombinedData(data:StatewiseItem)
    {
        val lastUpdatedTime=data.lastupdatedtime
        val simpleDateFormat =SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        lastUpdatedtv.text="LAST UPDATED\n ${gettimeago(simpleDateFormat.parse(lastUpdatedTime))}"
        confirmedTV.text=data.confirmed
        activeTV.text=data.active
        deathTV.text=data.deaths
        recoveredTV.text=data.recovered

    }
    fun gettimeago(past: Date):String{
        val now:Date=Date()
        val seconds=TimeUnit.MILLISECONDS.toSeconds(now.time-past.time)
        val minutes=TimeUnit.MILLISECONDS.toMinutes(now.time-past.time)
        val hours=TimeUnit.MILLISECONDS.toHours(now.time-past.time)
        return when{
            seconds<60->{
                "Few seconds ago"
            }
            minutes<60->{
                "few minutes ago"
            }
            hours<24->{
                "$hours hour ${minutes%60} min ago"
            }
            else->
            {
                SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(past).toString()
            }

        }
    }

}