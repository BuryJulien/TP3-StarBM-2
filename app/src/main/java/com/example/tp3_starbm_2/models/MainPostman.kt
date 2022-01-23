package com.example.tp3_starbm_2.models

import com.example.tp3_star.dataBase.entities.BusRoutes
import com.example.tp3_star.dataBase.entities.StopTimes
import com.example.tp3_star.dataBase.entities.Stops
import com.example.tp3_starbm_2.entities.Direction
import java.text.SimpleDateFormat
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

    private var date = SimpleDateFormat("yyyy-MM-dd").format(Date())
    private var heure = "00:00:00"

    private lateinit var route: BusRoutes
    private lateinit var tripDirection: Direction

    private val tripSubscribers = ArrayList<Observer>()

    fun tripPackageSubsrcibe(observer: Observer)
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

    fun getDate() : String?
    {
        System.out.println(date)
        return date
    }

    fun setDate(date: String)
    {
        System.out.println(date)
        this.date = date
        tripPackageNotify()
    }

    fun getHeure() : String
    {
        return heure
    }

    fun setHeure(heure : String)
    {
        this.heure = heure
        tripPackageNotify()
    }

    fun getRoute() : BusRoutes
    {
        return route
    }

    fun setRoute(route: BusRoutes)
    {
        this.route = route
    }

    fun getDirection() : Direction
    {
        return tripDirection
    }

    fun setDirection(direction: Direction)
    {
        this.tripDirection = direction
        tripPackageNotify()
    }

    //Stop package

    private lateinit var stop: Stops

    private val stopSubscribers = ArrayList<Observer>()

    fun stopPackageSubsrcibe(observer: Observer)
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

    fun setStop(stop : Stops)
    {
        this.stop = stop
        stopPackageNotify()
    }


    // Stop time package

    private lateinit var stopTime: StopTimes

    private val stopTimeSubscribers = ArrayList<Observer>()

    fun stopTimePackageSubsrcibe(observer: Observer)
    {
        stopTimeSubscribers.add(observer)
    }

    fun stopTimePackageUnsubscribe(observer: Observer)
    {
        stopTimeSubscribers.remove(observer)
    }

    private fun stopTimePackageNotify()
    {
        stopTimeSubscribers.forEach { it.update(this, null) }
    }

    fun getStopTime() : StopTimes
    {
        return stopTime
    }

    fun setStopTime(stopTime : StopTimes)
    {
        this.stopTime = stopTime
        stopTimePackageNotify()
    }








}