package com.example.alivecor.viewmodel

import android.service.autofill.UserData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alivecor.model.Userdata

class SharedViewModel: ViewModel() {


   val userData =MutableLiveData<Userdata>()

  fun setData(data:Userdata){
    userData.run {
      setValue(data)
    }
  }
}