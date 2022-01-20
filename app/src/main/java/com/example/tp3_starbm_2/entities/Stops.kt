package com.example.tp3_star.dataBase.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Stops (
    @PrimaryKey val stop_id : Int,
    @ColumnInfo(name="stop_name") val stop_name : String,
    @ColumnInfo(name="stop_desc") val stop_desc : String,
    @ColumnInfo(name="stop_lat") val stop_lat : Double,
    @ColumnInfo(name="stop_lon") val stop_lon : Double,
    @ColumnInfo(name="wheelchair_boarding") val wheelchair_boarding : Boolean,
)