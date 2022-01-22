package com.example.tp3_starbm_2.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.database.DatabaseUtils
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.tp3_star.dataBase.entities.BusRoutes
import com.example.tp3_starbm_2.CustomAdapter
import com.example.tp3_starbm_2.R
import com.example.tp3_starbm_2.contract.StarContract
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var ll = inflater.inflate(R.layout.fragment_filter, container, false)
        val butValidFilter: Button = ll.findViewById(R.id.butValidFilter);

        butValidFilter.setOnClickListener{
            this.openFragmentStops()
        }

        val butChangeHour: Button = ll.findViewById<Button>(R.id.butChangeHour)
        val textViewHour: TextView = ll.findViewById<TextView>(R.id.textViewHour)

        spinnerRoutes = ll.findViewById<Spinner>(R.id.spinnerLignesBus)

        butChangeHour.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                textViewHour.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        val butChangeDate = ll.findViewById<Button>(R.id.butChangeDate)
        val textViewDate = ll.findViewById<TextView>(R.id.textViewDate)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        butChangeDate.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                textViewDate?.setText("" + dayOfMonth + " " + month + ", " + year)
            }, year, month, day)
            dpd.show()
        }
        loadBusRoutes()

        return ll
    }

    private fun openFragmentStops() {
        val stopsFragment = StopsFragment()
        val fragmentManager = this.parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.add(R.id.filterFragment, stopsFragment, "BLANK_FRAGMENT").commit()
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
                System.out.println("CURSOR ---------------------")
                //System.out.println(DatabaseUtils.dumpCursorToString(cursor))
                System.out.println("END CURSOR ---------------------")
                var id = 0
                while (cursor.moveToNext())
                {
                    id++
                    val short = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME))
                    val name = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.LONG_NAME))
                    val color = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.COLOR))
                    val textColor = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR))
                    routes.add(BusRoutes(id,short, name, color, textColor))
                }
            }
        }

        val adapter = CustomAdapter(requireActivity(), routes)
        spinnerRoutes.adapter = adapter

        spinnerRoutes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

    }

    @SuppressLint("Range")
    private fun loadBusDirections(ll: View)
    {
        val contentResolver = requireActivity().contentResolver
        val uri = StarContract.Trips.CONTENT_URI
        val cursor = contentResolver.query(uri, null,
            spinnerRoutes.selectedItem as String?,null,null)
        if (cursor != null) {
            if(cursor.count > 0)
            {
                System.out.println("CURSOR ---------------------")
                //System.out.println(DatabaseUtils.dumpCursorToString(cursor))
                System.out.println("END CURSOR ---------------------")
                var id = 0
                while (cursor.moveToNext())
                {
                    id++
                    val short = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME))
                    val name = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.LONG_NAME))
                    val color = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.COLOR))
                    val textColor = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR))
                    routes.add(BusRoutes(id,short, name, color, textColor))
                }
            }
        }

        val adapter = CustomAdapter(requireActivity(), routes)
        spinnerRoutes.adapter = adapter
        spinnerRoutes.onItemSelectedListener = this
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