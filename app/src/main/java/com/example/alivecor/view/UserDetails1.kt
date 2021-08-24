package com.example.alivecor.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.alivecor.R
import com.example.alivecor.model.Userdata
import com.example.alivecor.showToast
import com.example.alivecor.util.DateFomatter
import com.example.alivecor.viewmodel.SharedViewModel
import java.util.*
import kotlin.collections.ArrayList

class UserDetails1 : Fragment() , View.OnClickListener{

    lateinit var navController : NavController
    private var sharedViewModel: SharedViewModel?=null
    var monthOptions =
        arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec")

    private lateinit var mDateSpinner: Spinner
    private lateinit var mMonthSpinner:Spinner
    private lateinit var mYearSpinner:Spinner

    var dateOptions: ArrayList<String>? = null
    lateinit var yearOptions:ArrayList<String>

    private lateinit var  firstName :TextView
    private lateinit var  lastName :TextView
    private lateinit var  next : Button

    private var currentItem = 0
    private var currentYearItem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
         next= view.findViewById(R.id.next_btn)

         firstName= view.findViewById(R.id.input_username)
         lastName= view.findViewById(R.id.input_lastname)

        mDateSpinner = view.findViewById(R.id.datePick_spinner)
        mMonthSpinner =view.findViewById(R.id.monthPick_spinner)
        mYearSpinner = view.findViewById(R.id.yearPick_spinner)
        setInitialValues()

        sharedViewModel = activity?.run {
            ViewModelProvider(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        next.setOnClickListener {

            if(validate()){

                //age
                val myearSelected = mYearSpinner.getSelectedItem().toString()
                val monthSelected = mMonthSpinner.getSelectedItemPosition() + 1
                val day = mDateSpinner.getSelectedItem().toString()

                val age = DateFomatter.getUserAge(
                    myearSelected.toInt(),
                    monthSelected.toString().toInt(),
                    day.toInt()
                )

                val dataClass = Userdata(firstName.text.toString(),lastName.text.toString(),age)

                sharedViewModel!!.setData(dataClass)
                navController.navigate(R.id.action_userDetails1_to_userDetails2)
            }

        }

    }

    override fun onClick(v: View?) {

        navController!!.navigate(R.id.action_userDetails1_to_userDetails2)
    }

    private fun setInitialValues(){
        fillDateSpinner()
        fillMonthSpinner()
        fillYearSpinner()
    }

    private fun fillDateSpinner() {
        dateOptions = ArrayList()
        for (i in 1..31) {
            val date = i.toString()
            dateOptions!!.add(date)
        }
        // use default spinner item to show options in spinner
        val dateAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                dateOptions!!
            )
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDateSpinner.adapter = dateAdapter
        dateAdapter.notifyDataSetChanged()
        mDateSpinner.setSelection(0) //Default value will be 1 always
    }


    private fun fillMonthSpinner() {

        // use default spinner item to show options in spinner
        val monthPickAdapter=
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                monthOptions
            )
        monthPickAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mMonthSpinner.adapter = monthPickAdapter
        monthPickAdapter.notifyDataSetChanged()
        mMonthSpinner.setSelection(0) //default value will be Jan always
    }


    private fun fillYearSpinner() {
        yearOptions = ArrayList<String>()
        val previous50Year: String = java.lang.String.valueOf(DateFomatter.getPrevious50year())
        val initialYear: Int = DateFomatter.getPrevious120year()
        var currentYear: Int = Calendar.getInstance().get(Calendar.YEAR)

        // use default spinner item to show options in spinner
        for (j in initialYear..currentYear) {
            val yearData = j.toString()
            yearOptions.add(yearData)
        }
        val yearPickAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, yearOptions)
        yearPickAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mYearSpinner.adapter = yearPickAdapter
        yearPickAdapter.notifyDataSetChanged()
        mYearSpinner.setSelection(previous50Year.toInt()) //default value will be 50 years lesser than current year always
    }

    private fun validate() : Boolean{

        if (firstName.text.toString().trim().isEmpty()){
            context?.showToast("Please enter first name")
            return false

        }

        else if (lastName.text.toString().trim().isEmpty()){
            context?.showToast("Please enter last name")
            return false

        }

        else {
            val myearSelected = mYearSpinner.selectedItem.toString()
            val monthSelected = mMonthSpinner.selectedItemPosition + 1
            val day = mDateSpinner.selectedItem.toString()

            val daysInMonth =
                getNoOfDaysInAMonth(myearSelected.toInt(), monthSelected.toString().toInt())

            val monSelected = mMonthSpinner.selectedItem.toString()
            var yearSelected = 0
            var isLeapYear = false
            yearSelected = myearSelected.toInt()
            isLeapYear =
                yearSelected % 4 == 0 && currentYearItem % 100 != 0 || currentYearItem % 400 == 0


            if (day.toInt() > daysInMonth) {
                if (monSelected.contains("Feb") && !isLeapYear) {
                    context?.showToast("Wrong year input($yearSelected isn't a leap year)")
                } else {
                    context?.showToast("Wrong Date Input")
                }
                return false
            }
            val age = DateFomatter.getAge(
                myearSelected.toInt(),
                monthSelected.toString().toInt(),
                day.toInt()
            )

            if (Integer.parseInt(age)<0){
                context?.showToast("Please enter valid date")
                return false
            }
        }

        return true
    }


    private fun getNoOfDaysInAMonth(iYear: Int, iMonth: Int): Int {
        return if (iMonth != 2) {
            31 - (iMonth - 1) % 7 % 2
        } else {
            if (iYear and 3 == 0 && (iYear % 25 != 0 || iYear and 15 == 0)) { // leap yearselected
                29
            } else {
                28
            }
        }
    }
}