package com.example.tp3_starbm_2

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.tp3_star.dataBase.entities.BusRoutes
import com.example.tp3_starbm_2.entities.Direction

class CustomAdapterDirection : BaseAdapter {
    private var context: Context
    private var inflter: LayoutInflater
    private var listDirections: List<Direction>
    private var busRoute: BusRoutes

    public constructor(applicationContext: Context, listDirections: List<Direction>, busRoute: BusRoutes){
        this.context = applicationContext
        this.inflter = LayoutInflater.from(applicationContext)
        this.listDirections = listDirections
        this.busRoute = busRoute
    }

    override fun getCount(): Int {
        return this.listDirections.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        val view = this.inflter.inflate(R.layout.custom_spinner_item, null)
        val libelle = view.findViewById<TextView>(R.id.libelleSpinner)
        val direction = this.listDirections.get(i)
        libelle.setText(direction.trip_headsign)

        //libelle.setText(busRoute.route_long_name)

        libelle.setTextColor(Color.parseColor("#" + busRoute.route_text_color))
        view.setBackgroundColor(Color.parseColor("#" + busRoute.route_color))
        view.setPadding(30, 30, 0, 30)
        return view
    }

}