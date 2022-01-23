package com.example.tp3_starbm_2.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.database.DatabaseUtils
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
import com.example.tp3_star.dataBase.entities.StopTimes
import com.example.tp3_star.dataBase.entities.Stops
import com.example.tp3_starbm_2.R
import com.example.tp3_starbm_2.contract.StarContract
import com.example.tp3_starbm_2.interfaces.IOnBackPressed
import com.example.tp3_starbm_2.models.MainPostman
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
class HoursFragment() : Fragment() , Observer, IOnBackPressed{
    // TODO: Rename and change types of parameters
    val postMan = MainPostman
    private var isLandscape = false
    private var listHours = ArrayList<StopTimes>()
    private lateinit var layoutListStopTimes: LinearLayout
    private var canOpen = true


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isLandscape = this.resources.configuration.orientation === Configuration.ORIENTATION_LANDSCAPE
        val ll = inflater.inflate(R.layout.fragment_hours, container, false)
        val butCancelHours: Button = ll.findViewById(R.id.butCancelHours)
        this.layoutListStopTimes = ll.findViewById(R.id.layoutListStopTimes)
        postMan.stopPackageSubsrcibe(this)

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
        val cursor = contentResolver.query(uri, null,null, arrayOf<String>(postMan.getStop().stop_id.toString(), postMan.getRoute().route_id.toString(), postMan.getDirection().direction_id.toString(), postMan.getDate().toString(), postMan.getHeure()),null)
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
            val stopTime = it
            tv.setOnClickListener{
                postMan.setStopTime(stopTime)
                this.openFragment()
            }
            layoutListStopTimes.addView(tv)
        }
    }


    private fun openFragment() {
        if(this.canOpen) {
            val fragment = StopTimesDetailFragment()
            val hoursFragment = HoursFragment()
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
                fragmentTransaction2.add(R.id.frag1, hoursFragment, "BLANK_FRAGMENT").commit()
                fragmentTransaction.replace(R.id.frag2, fragment, "BLANK_FRAGMENT").commit()
            }
            else
            {
                fragmentTransaction.add(R.id.filterFragment, fragment, "BLANK_FRAGMENT").commit()
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

    override fun onBackPressed(): Boolean {
        postMan.stopPackageUnsubscribe(this)
        return true
    }
}