package com.example.covidtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_main.view.confirmedTV
import kotlinx.android.synthetic.main.activity_main.view.deathTV
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.item_list.view.activeTV
import kotlinx.android.synthetic.main.item_list.view.recoveredTV

class listadapter(val list:List<StatewiseItem>):BaseAdapter(){
    override fun getCount()=list.size



    override fun getItem(p0: Int)=list[p0]

    override fun getItemId(p0: Int)=p0.toLong()


    override fun getView(position: Int, convertview: View?, parent: ViewGroup?): View? {
        val view=convertview?:LayoutInflater.from(parent?.context).inflate(R.layout.item_list,parent,false)
        val item=list[position]
        view.confirmedTV.text=SpannableDelta("${item.confirmed}\n ↑ ${item.deltaconfirmed?:"0"}","#ff0000",item.confirmed?.length?:0)
        view.activeTV.text=item.active
        view.recoveredTV.text=SpannableDelta("${item.recovered}\n ↑ ${item.deltarecovered?:"0"}","#00ff00",item.recovered?.length?:0)
        view.deathTV.text=SpannableDelta("${item.deaths}\n ↑ ${item.deltadeaths?:"0"}","#ffff00",item.deaths?.length?:0)
        view.stateTV.text=item.state
        return view
    }

}