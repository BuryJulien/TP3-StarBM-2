package com.example.tp3_starbm_2.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentContainerView
import com.example.tp3_starbm_2.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChangeDate()
        initChangeHour()

        val ll = inflater.inflate(R.layout.fragment_filter, container, false)
        val butValidFilter: Button = ll.findViewById(R.id.butValidFilter);

        butValidFilter.setOnClickListener{
            this.openFragmentStops()
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

    fun initChangeHour(){
        val butChangeHour = view?.findViewById<Button>(R.id.butChangeHour)
        val textViewHour = view?.findViewById<TextView>(R.id.textViewHour)

        butChangeHour?.setOnClickListener {
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                textViewHour?.text = SimpleDateFormat("HH:mm").format(cal.time)
            }
            TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
    }

    fun initChangeDate(){
        val butChangeDate = view?.findViewById<Button>(R.id.butChangeDate)
        val textViewDate = view?.findViewById<TextView>(R.id.textViewDate)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        butChangeDate?.setOnClickListener {
            val dpd = DatePickerDialog(requireActivity(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                textViewDate?.setText("" + dayOfMonth + " " + month + ", " + year)
            }, year, month, day)
            dpd.show()
        }
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