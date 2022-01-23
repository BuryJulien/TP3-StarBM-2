package com.example.tp3_star.dataBase.entities

data class Stops(
    val stop_id: Int,
    val stop_name: String,
    val description: String,
    val latitude: String?,
    val longitude: String?,
    val wheelchair_bording: String?
)