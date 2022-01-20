package com.example.tp3_starbm_2.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CpuUsageInfo
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tp3_star.dataBase.entities.BusRoutes
import com.example.tp3_star.dataBase.entities.Stops
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
class FilterFragment : Fragment() {
    val postMan = MainPostman
    private var routes = ArrayList<BusRoutes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val ll = inflater.inflate(R.layout.fragment_filter, container, false)
        val butValidFilter: Button = ll.findViewById(R.id.butValidFilter);

        butValidFilter.setOnClickListener{
            this.openFragmentStops()
        }

        val butChangeHour: Button = ll.findViewById<Button>(R.id.butChangeHour)
        val textViewHour: TextView = ll.findViewById<TextView>(R.id.textViewHour)

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
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            //ActivityCompat.requestPermissions()
        }
        val contentResolver = requireActivity().contentResolver
        val uri = StarContract.BusRoutes.CONTENT_URI
        val cursor = contentResolver.query(uri, null,null,null,null)
        if (cursor != null) {
            if(cursor.count > 0)
            {
                var id = 0
                while (cursor.moveToNext())
                {
                    id++
                    var short = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME))
                    var name = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.LONG_NAME))
                    var color = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.COLOR))
                    var textColor = cursor.getString(cursor.getColumnIndex(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR))
                    routes.add(BusRoutes(id,short, name, color, textColor))
                }
            }
        }
        val spinnerRoutes = requireActivity().findViewById<Spinner>(R.id.spinnerLignesBus)

        val adapter = CustomAdapter(requireActivity(), routes)
        spinnerRoutes.adapter = adapter

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