package com.example.tp3_starbm_2.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.tp3_star.dataBase.entities.Stops
import com.example.tp3_starbm_2.R
import com.example.tp3_starbm_2.contract.StarContract
import com.example.tp3_starbm_2.models.MainPostman
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StopsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StopsFragment constructor() : Fragment(), Observer {
    val postMan = MainPostman
    private var param1: String? = null
    private var param2: String? = null
    private var listStops = ArrayList<Stops>()
    private lateinit var layoutListStops: LinearLayout
    private var canOpen = true
    private var isLandscape = false



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isLandscape = this.resources.configuration.orientation === Configuration.ORIENTATION_LANDSCAPE
        val ll = inflater.inflate(R.layout.fragment_stops, container, false)
        val butCancelStops: Button = ll.findViewById(R.id.butCancelStops)
        this.layoutListStops = ll.findViewById(R.id.layoutListStops)

        postMan.tripPackageSubsrcibe(this)

        butCancelStops.setOnClickListener{
            this.activity?.onBackPressed()
        }

        if(isLandscape) {
            butCancelStops.visibility = View.INVISIBLE
        }

        this.loadStops()

        // Inflate the layout for this fragment
        return ll
    }

    @SuppressLint("Range")
    private fun loadStops() {
        val contentResolver = requireActivity().contentResolver
        val uri = StarContract.Stops.CONTENT_URI
        System.out.println("Parameters : " + postMan.getRoute().route_id.toString() + " " + postMan.getDirection().direction_id.toString())
        val cursor = contentResolver.query(uri, null,null, arrayOf<String>(postMan.getRoute().route_id.toString(), postMan.getDirection().direction_id.toString()),null)
        if (cursor != null) {
            if(cursor.count > 0)
            {
                listStops.clear()
                System.out.println("CURSOR ---------------------")
                // System.out.println(DatabaseUtils.dumpCursorToString(cursor))
                System.out.println("END CURSOR ---------------------")
                while (cursor.moveToNext())
                {
                    val id = cursor.getInt(cursor.getColumnIndex(StarContract.Stops.CONTENT_PATH + StarContract.Stops.StopColumns._ID))
                    val description = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.DESCRIPTION))
                    val name = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.NAME))
                    val latitude = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.LATITUDE))
                    val longitude = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.LONGITUDE))
                    val wheelchair = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING))
                    addOrReplaceOnDuplicate(Stops(id ,name, description, latitude, longitude, wheelchair))
                }
            }
        }
        layoutListStops.removeAllViews()
        this.listStops.forEach{
            val sep: TextView = TextView(this.context)
            sep.setText("  ||")
            layoutListStops.addView(sep)

            val tv: TextView = TextView(this.context)
            val text: String = it.stop_name + " - " + it.description
            tv.setText(text)
            tv.textSize = 20F
            val stop = it
            tv.setOnClickListener{
                postMan.setStop(stop)
                this.openFragment()
            }
            layoutListStops.addView(tv)
        }

    }

    fun addOrReplaceOnDuplicate(stop: Stops)
    {
        var stopToRemove : Stops? = null
        listStops.forEach {
            if(it.stop_name == stop.stop_name)
            {
                stopToRemove = it
            }
        }
        if(stopToRemove != null)
        {
            listStops.remove(stopToRemove)
        }
        listStops.add(stop)
    }

    private fun openFragment() {
        if(this.canOpen) {
            val hoursFragment = HoursFragment()
            val stopsFragment = StopsFragment()
            val fragmentManager = this.parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.slide_out,
                R.anim.slide_in,
                R.anim.slide_out
            )
            fragmentTransaction.addToBackStack(null)

            val fragmentTransaction2 = fragmentManager.beginTransaction()
            fragmentTransaction2.addToBackStack(null)
            if(isLandscape)
            {
                fragmentTransaction2.add(R.id.frag1, stopsFragment, "BLANK_FRAGMENT").commit()
                fragmentTransaction.replace(R.id.frag2, hoursFragment, "BLANK_FRAGMENT").commit()
            }
            else
            {
                fragmentTransaction.add(R.id.filterFragment, hoursFragment, "BLANK_FRAGMENT").commit()
            }

            val handler = Handler(Looper.getMainLooper())
            this.canOpen = false
            handler.postDelayed({
                this.canOpen = true
            }, 1000)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StopsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            StopsFragment()
    }

    override fun update(p0: Observable?, p1: Any?) {
        loadStops()
    }
}