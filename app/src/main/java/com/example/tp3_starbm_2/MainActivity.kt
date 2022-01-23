package com.example.tp3_starbm_2

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.FragmentContainerView
import com.example.tp3_starbm_2.fragments.StopsFragment
import com.example.tp3_starbm_2.interfaces.IOnBackPressed
import com.example.tp3_starbm_2.models.MainPostman

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onBackPressed() {
        if(this.resources.configuration.orientation === Configuration.ORIENTATION_LANDSCAPE)
        {
            val fragment =
                this.supportFragmentManager.findFragmentById(R.id.frag2)
            (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
                super.onBackPressed()
            }
        }
        else
        {
            val fragment =
                this.supportFragmentManager.findFragmentById(R.id.filterFragment)
            (fragment as? IOnBackPressed)?.onBackPressed()?.not()?.let {
                super.onBackPressed()
            }
        }

    }
}