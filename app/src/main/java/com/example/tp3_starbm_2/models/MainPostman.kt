package com.example.tp3_starbm_2.models

import android.annotation.SuppressLint
import android.content.Context
import com.example.tp3_star.dataBase.entities.BusRoutes
import com.example.tp3_star.dataBase.entities.Stops
import com.example.tp3_starbm_2.MainActivity
import com.example.tp3_starbm_2.contract.StarContract
import com.example.tp3_starbm_2.entities.Trips
import java.util.*
import kotlin.collections.ArrayList

object MainPostman : Observable() {


    var globalSubscribers = ArrayList<Observer>()

    fun subscribe(observer: Observer) {
        globalSubscribers.add(observer)
    }

    fun unsubscribe(observer: Observer) {
        globalSubscribers.remove(observer)
    }

    fun globalNotify() {
        globalSubscribers.forEach { it.update(this, null) }
    }

    //Trip package

    private lateinit var route: BusRoutes
    private lateinit var trip: Trips

    private val tripSubscribers = ArrayList<Observer>()

    fun tripPackageSubrcibe(observer: Observer)
    {
        tripSubscribers.add(observer)
    }

    fun tripPackageUnsubscribe(observer: Observer)
    {
        tripSubscribers.remove(observer)
    }

    private fun tripPackageNotify()
    {
        tripSubscribers.forEach { it.update(this, null) }
    }

    fun getRoute() : BusRoutes
    {
        return route
    }

    fun setRoute(route: BusRoutes)
    {
        this.route = route
        tripPackageNotify()
    }

    fun getTrip() : Trips
    {
        return trip
    }

    //Stop package

    private lateinit var stop: Stops

    private val stopSubscribers = ArrayList<Observer>()

    fun stopPackageSubrcibe(observer: Observer)
    {
        stopSubscribers.add(observer)
    }

    fun stopPackageUnsubscribe(observer: Observer)
    {
        stopSubscribers.remove(observer)
    }

    private fun stopPackageNotify()
    {
        stopSubscribers.forEach { it.update(this, null) }
    }

    fun getStop() : Stops
    {
        return stop
    }








}