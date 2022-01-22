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
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HoursFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HoursFragment(private var stop: String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listHours = ArrayList<StopTimes>()
    private lateinit var layoutListStopTimes: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val ll = inflater.inflate(R.layout.fragment_hours, container, false)
        val butCancelHours: Button = ll.findViewById(R.id.butCancelHours)
        this.layoutListStopTimes = ll.findViewById(R.id.layoutListStopTimes)

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
        val cursor = contentResolver.query(uri, null,null, arrayOf<String>("1005", "0001", "0", "0", "1", "0", "0", "0", "0", "0"),null)
        if (cursor != null) {
            if(cursor.count > 0)
            {
                System.out.println("CURSOR ---------------------")
                System.out.println(DatabaseUtils.dumpCursorToString(cursor))
                System.out.println("END CURSOR ---------------------")
                var id = 0
                while (cursor.moveToNext())
                {
                    id++
                    val trip_id = cursor.getString(cursor.getColumnIndex(StarContract.StopTimes.StopTimeColumns.TRIP_ID))
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
                }
            }
        }

        this.listHours.forEach{
            val sep: TextView = TextView(this.context)
            sep.setText("  ||")
            layoutListStopTimes.addView(sep)

            val tv: TextView = TextView(this.context)
            val text: String = it.departure_time
            tv.setText(text)
            tv.textSize = 20F
            tv.setOnClickListener{
                this.openFragment(text)
            }
            layoutListStopTimes.addView(tv)
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
        fun newInstance(param1: String, param2: String, stop: String) =
            HoursFragment(stop).apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}