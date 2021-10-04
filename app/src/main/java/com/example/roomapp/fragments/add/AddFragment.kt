package com.example.roomapp.fragments.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.roomapp.R
import com.example.roomapp.model.User
import com.example.roomapp.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.fragment_add, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        mView.add_btn.setOnClickListener {
            insertDataToDatabase()

        }
        mView.btn_datepicker.setOnClickListener {
            openDatePicker()
        }

        return  mView
    }
private fun openDatePicker(){
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    val dpd = DatePickerDialog(
        requireActivity(),
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            val d = Date(year, month, day)
            val dateFormatter = SimpleDateFormat(
                "MM-dd-yyyy"
            )
           var strDate = dateFormatter.format(d)
            mView.txt_showdate.text=strDate
        },
        year,
        month,
        day
    )

    dpd.show()


}
    private fun insertDataToDatabase() {
        val firstName = addFirstName_et.text.toString()
        val lastName = addLastName_et.text.toString()
        val age = addAge_et.text

        if(inputCheck(firstName, lastName, age)){
            // Create User Object
            val user = User(
                0,
                firstName,
                lastName,
                Integer.parseInt(age.toString())
            )
            // Add Data to Database
            lifecycleScope.launchWhenStarted {
                mUserViewModel.addUser(user)
            }
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean{
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }

}