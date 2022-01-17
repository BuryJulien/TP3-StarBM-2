package com.example.tp3_starbm_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tp3_starbm_2.models.MainPostman

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        MainPostman.setMainActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}