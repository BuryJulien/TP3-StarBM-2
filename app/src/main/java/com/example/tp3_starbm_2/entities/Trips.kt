package com.example.tp3_starbm_2.entities

data class Trips (
    val route_id : Int,
    val service_id : Double,
    val trip_headsign : String,
    val direction_id : Int,
    val block_id : String,
    val wheelchair_accessible : Boolean,
)