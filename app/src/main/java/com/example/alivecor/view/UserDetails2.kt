package com.example.alivecor.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alivecor.R
import com.example.alivecor.viewmodel.SharedViewModel


class UserDetails2 : Fragment() {

    lateinit var  sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_details2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName= view.findViewById<TextView>(R.id.username)
        val lastName= view.findViewById<TextView>(R.id.lastname)
        val age= view.findViewById<TextView>(R.id.age)
        sharedViewModel = activity?.run {
            ViewModelProvider(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        sharedViewModel.userData.observe(viewLifecycleOwner,
            { t ->
                userName.text = t?.userName
                lastName.text = t?.lastName
                age.text = t?.age
            })


    }

}