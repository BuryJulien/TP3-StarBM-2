package com.example.tp3_star.dataBase.entities

data class StopTimes(
    val trip_id: String,
    val arrival_time: String,
    val departure_time: String,
    val stop_id: String,
    val stop_sequence: String
)