package com.example.tp3_starbm_2.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import com.example.tp3_star.dataBase.entities.BusRoutes
import com.example.tp3_starbm_2.CustomAdapter
import com.example.tp3_starbm_2.CustomAdapterDirection
import com.example.tp3_starbm_2.R
import com.example.tp3_starbm_2.contract.StarContract
import com.example.tp3_starbm_2.entities.Direction
import com.example.tp3_starbm_2.models.MainPostman
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [FilterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FilterFragment : Fragment(), AdapterView.OnItemSelectedListener {
    val postMan = MainPostman
    private var routes = ArrayList<BusRoutes>()
    private lateinit var spinnerRoutes : Spinner
    private var directions = ArrayList<Direction>()
    private lateinit var spinnerDirections : Spinner
    private lateinit var butChangeHour : Button
    private lateinit var textViewHour : TextView
    private lateinit var butValidFilter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var ll = inflater.inflate(R.layout.fragment_filter, container, false)
        butValidFilter = ll.findViewById(R.id.butValidFilter);

        butValidFilter.setOnClickListener{
            this.openFragmentStops()
        }

        this.butChangeHour = ll.findViewById<Button>(R.id.butChangeHour)
        this.textViewHour = ll.findViewById<TextView>(R.id.textViewHour)

        spinnerRoutes = ll.findViewById<Spinner>(R.id.spinnerLignesBus)
        spinnerDirections = ll.findViewById<Spinner>(R.id.spinnerDirections)

        butChangeHour.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                textViewHour.text = SimpleDateFormat("HH:mm").format(cal.time)
                postMan.setHeure(SimpleDateFormat("HH:mm:ss").format(cal.time))
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        val butChangeDate = ll.findViewById<Button>(R.id.butChangeDate)
        val textViewDate = ll.findViewById<TextView>(R.id.textViewDate)

        val search = ll.findViewById<SearchView>(R.id.searchView)
        search.setOnCloseListener{
            System.out.println("HEY")
            true
        }
        search.setOnSearchClickListener{
            System.out.println("HEY2")
        }
        search.setOnFocusChangeListener { view, b ->
            System.out.println("HEY3")
        }

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        butChangeDate.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                textViewDate?.setText("" + dayOfMonth + ", " + month + ", " + year)
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                postMan.setDate(SimpleDateFormat("yyyy-MM-dd").format(calendar.time))
            }, year, month, day)
            dpd.show()
        }
        loadBusRoutes()

        return ll
    }

    private fun openFragmentStops() {
        this.butValidFilter.isEnabled = false
        val stopsFragment = StopsFragment()
        val fragmentManager = this.parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        if(this.resources.configuration.orientation === Configuration.ORIENTATION_LANDSCAPE) {
            fragmentTransaction.replace(R.id.stopsFragment, stopsFragment, "BLANK_FRAGMENT").commit()
        } else {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.add(R.id.filterFragment, stopsFragment, "BLANK_FRAGMENT").commit()
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            this.butValidFilter.isEnabled = true
        }, 1000)
    }

    @SuppressLint("Range")
    private fun loadBusRoutes()
    {
        val contentResolver = requireActivity().contentResolver
        val uri = StarContract.BusRoutes.CONTENT_URI
        val cursor = contentResolver.query(uri, null,null,null,null)
        if (cursor != null) {
            if(cursor.count > 0)
            {
                while (cursor.moveToNext())
                {
                    val id = cursor.getString(cursor.getColumnIndex("route" + StarContract.BusRoutes.BusRouteColumns._ID))
                    var short = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME))
                    var name = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.LONG_NAME))
                    var color = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.COLOR))
                    var textColor = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR))
                    routes.add(BusRoutes(id.toInt(),short, name, color, textColor))
                }
            }
        }

        val adapter = CustomAdapter(requireActivity(), routes)
        spinnerRoutes.adapter = adapter
        if(routes.isNotEmpty()) postMan.setRoute(routes.get(0))

        spinnerRoutes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                postMan.setRoute(routes.get(p2))
                loadBusDirections()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }

    @SuppressLint("Range")
    private fun loadBusDirections()
    {
        val contentResolver = requireActivity().contentResolver
        val uri = StarContract.Trips.CONTENT_URI
        val cursor = contentResolver.query(uri, null,
            postMan.getRoute().route_id.toString(),null,null)
        if (cursor != null) {
            if(cursor.count > 0)
            {
                directions.clear()
                while (cursor.moveToNext())
                {
                    val id = cursor.getInt(cursor.getColumnIndex(StarContract.Trips.TripColumns.DIRECTION_ID))
                    val name = cursor.getString(cursor.getColumnIndex(StarContract.Trips.TripColumns.HEADSIGN))
                    var i = true
                    directions.forEach {
                        if(it.direction_id == id)
                        {
                            it.trip_headsign += "/ " + name
                            i = false
                    }}
                    if(i) directions.add(Direction(id, name))
                }
            }
        }

        val adapter = CustomAdapterDirection(requireActivity(), directions, postMan.getRoute())
        spinnerDirections.adapter = adapter
        spinnerDirections.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                postMan.setDirection(directions.get(p2))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    override fun onItemSelected(adaptor: AdapterView<*>?, view: View?, position: Int, id: Long) {
        /*
        val busRoutes: List<BusRoutes> = dbManager.getRoutes()
        this.selectedBusRoute = busRoutes.get(position)
        this.initDirections()

         */
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FilterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FilterFragment()
    }

}