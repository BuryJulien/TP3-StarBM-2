package com.example.tp3_star.dataBase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BusRoutes (
    @PrimaryKey val route_id : Int,
    @ColumnInfo(name="route_short") val route_short : String,
    @ColumnInfo(name="route_long_name") val route_long_name : String,
    @ColumnInfo(name="route_desc") val route_desc : String,
    @ColumnInfo(name="route_type") val route_type : String,
    @ColumnInfo(name="route_color") val route_color : String,
    @ColumnInfo(name="route_text_color") val route_text_color : String,
)