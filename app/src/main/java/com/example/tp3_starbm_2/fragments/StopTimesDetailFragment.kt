package com.example.tp3_starbm_2.fragments

import android.annotation.SuppressLint
import android.database.DatabaseUtils
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.tp3_star.dataBase.entities.StopTimes
import com.example.tp3_star.dataBase.entities.Stops
import com.example.tp3_starbm_2.R
import com.example.tp3_starbm_2.contract.StarContract
import com.example.tp3_starbm_2.models.MainPostman
import java.math.BigDecimal
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HoursFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StopTimesDetailFragment() : Fragment(), Observer {
    // TODO: Rename and change types of parameters
    val postMan = MainPostman
    private var param1: String? = null
    private var param2: String? = null
    private var listHours = ArrayList<StopTimes>()
    private var listStops = ArrayList<Stops>()
    private lateinit var layoutListStopTimes: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ll = inflater.inflate(R.layout.fragment_stop_times_detail, container, false)
        val butCancelHours: Button = ll.findViewById(R.id.butCancelHours)
        this.layoutListStopTimes = ll.findViewById(R.id.layoutListStopTimesDetail)
        postMan.stopTimePackageSubsrcibe(this)

        butCancelHours.setOnClickListener{
            this.activity?.onBackPressed()
        }

        this.loadHours()

        // Inflate the layout for this fragment
        return ll
    }

    @SuppressLint("Range")
    private fun loadHours() {
        val contentResolver = requireActivity().contentResolver
        val uri = StarContract.StopTimes.CONTENT_URI
        System.out.println("------------------ TRIP ID ---------- " + BigDecimal(postMan.getStopTime().trip_id).toPlainString())
        val cursor = contentResolver.query(uri, null,BigDecimal(postMan.getStopTime().trip_id).toPlainString(), null,null)
        if (cursor != null) {
            if(cursor.count > 0)
            {
                while (cursor.moveToNext())
                {
                    val trip_id = cursor.getDouble(cursor.getColumnIndex(StarContract.StopTimes.StopTimeColumns.TRIP_ID))
                    val arrival_time = cursor.getString(cursor.getColumnIndex(StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME))
                    val departure_time = cursor.getString(cursor.getColumnIndex(StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME))
                    val stop_id = cursor.getString(cursor.getColumnIndex(StarContract.StopTimes.StopTimeColumns.STOP_ID))
                    val stop_sequence = cursor.getString(cursor.getColumnIndex(StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE))
                    listHours.add(
                        StopTimes(
                            trip_id,
                            arrival_time,
                            departure_time,
                            stop_id,
                            stop_sequence
                        )
                    )
                    val id = cursor.getInt(cursor.getColumnIndex(StarContract.Stops.CONTENT_PATH + StarContract.Stops.StopColumns._ID))
                    val description = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.DESCRIPTION))
                    val name = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.NAME))
                    val latitude = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.LATITUDE))
                    val longitude = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.LONGITUDE))
                    val wheelchair = cursor.getString(cursor.getColumnIndex(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING))
                    listStops.add(Stops(id ,name, description, latitude, longitude, wheelchair))

                }
            }
        }

        this.listHours.forEachIndexed{ index, it ->

            if(it.stop_sequence.toInt() >= postMan.getStopTime().stop_sequence.toInt())
            {
                val sep: TextView = TextView(this.context)
                sep.setText("  ||")
                layoutListStopTimes.addView(sep)

                val tv: TextView = TextView(this.context)
                val text: String = it.departure_time + " -> " + listStops.get(index).stop_name
                tv.setText(text)
                tv.textSize = 20F
                val stopTime = it
                layoutListStopTimes.addView(tv)
            }

        }
    }

    private fun openFragment(text: String) {
        TODO("Not yet implemented")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HoursFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HoursFragment()
    }

    override fun update(p0: Observable?, p1: Any?) {
        loadHours()
    }
}