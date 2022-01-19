package com.example.tp3_starbm_2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.tp3_starbm_2.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StopsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StopsFragment constructor() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val ll = inflater.inflate(R.layout.fragment_stops, container, false)
        val butCancelStops: Button = ll.findViewById(R.id.butCancelStops)

        butCancelStops.setOnClickListener{
            this.activity?.onBackPressed()
        }

        val tabStops = arrayOf('1', '2', '3', '4', '5', '6', '7')
        val listStops: LinearLayout = ll.findViewById(R.id.listStops)
        tabStops.forEach {
            val tv: TextView = TextView(ll.context)
            val text: String = it.toString()
            tv.setText(text)
            tv.setOnClickListener{
                this.openFragment(text)
            }
            listStops.addView(tv)
        }

        // Inflate the layout for this fragment
        return ll
    }

    private fun openFragment(stop: String) {
        val hoursFragment = HoursFragment(stop)
        val fragmentManager = this.parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in, R.anim.slide_out)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.add(R.id.filterFragment, hoursFragment, "BLANK_FRAGMENT").commit()
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
        fun newInstance(param1: String, param2: String) =
            StopsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}