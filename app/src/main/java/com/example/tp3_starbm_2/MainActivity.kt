package com.example.tp3_starbm_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.FragmentContainerView
import com.example.tp3_starbm_2.fragments.StopsFragment
import com.example.tp3_starbm_2.models.MainPostman

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}