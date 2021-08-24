package com.example.alivecor.util

import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.Period
import java.util.*

class DateFomatter {

    companion object{
        fun getPrevious50year(): Int {
            val prevYear: Calendar = Calendar.getInstance()
            prevYear.add(Calendar.YEAR, -50)
            return prevYear.get(Calendar.YEAR)
        }

        fun getPrevious120year(): Int {
            val prevYear = Calendar.getInstance()
            prevYear.add(Calendar.YEAR, -120)
            return prevYear[Calendar.YEAR]
        }

        fun getAge(year: Int, month: Int, day: Int): String{
            val dob =
                Calendar.getInstance(TimeZone.getDefault())
            val today =
                Calendar.getInstance(TimeZone.getDefault())
            dob[year, month] = day
            val dob_month = dob[Calendar.MONTH]
            val dob_date = dob[Calendar.DAY_OF_MONTH]
            val today_month = today[Calendar.MONTH] + 1
            val today_day = today[Calendar.DAY_OF_MONTH]
            var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
            if (today_month < dob_month ||
                today_month == dob_month && today_day < dob_date
            ) {
                age--
            }
            val ageInt = age
            return ageInt.toString()
        }

        @SuppressLint("NewApi")
        fun getUserAge(year: Int, month: Int, day: Int) : String{
            val today: LocalDate = LocalDate.now()
            val birthday: LocalDate = LocalDate.of(year, month, day)

            val period: Period = Period.between(birthday, today)

            val age = period.getYears().toString() + " Yrs " +
                        period.getMonths().toString() + " Months " +
                        period.getDays().toString() + " Days"

            return age
        }

    }

}