package com.example.tp3_starbm_2.interfaces

interface Observable {
    fun subscribe(observer: Observer)
    fun unsubscribe(observer: Observer)
    fun globalNotify()
}