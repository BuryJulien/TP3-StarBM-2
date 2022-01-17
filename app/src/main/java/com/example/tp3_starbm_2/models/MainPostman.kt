package com.example.tp3_starbm_2.models

import android.annotation.SuppressLint
import android.content.Context
import com.example.tp3_starbm_2.MainActivity
import com.example.tp3_starbm_2.interfaces.Observable
import com.example.tp3_starbm_2.interfaces.Observer

object MainPostman : Observable {


    var globalSubscribers = ArrayList<Observer>()

    override fun subscribe(observer: Observer) {
        globalSubscribers.add(observer)
    }

    override fun unsubscribe(observer: Observer) {
        globalSubscribers.remove(observer)
    }

    override fun globalNotify() {
        globalSubscribers.forEach { it.update() }
    }

    // Main Activity package
    private lateinit var mainActivity : MainActivity

    fun setMainActivity(mainActivity: MainActivity) {
        System.out.println("----------------------- MAIN SET")
        System.out.println("is main non null : " + (mainActivity != null).toString())
        this.mainActivity = mainActivity
    }

    fun getMainActivity() : MainActivity{
        System.out.println("----------------------- MAIN GET")
        System.out.println("is main non null : " + (mainActivity != null).toString())
        return mainActivity
    }
}