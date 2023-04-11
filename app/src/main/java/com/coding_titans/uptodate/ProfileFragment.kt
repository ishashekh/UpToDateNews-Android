package com.coding_titans.uptodate

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController

class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val email = ProfileFragmentArgs.fromBundle(requireArguments()).email

        val userNameTextView = view.findViewById<TextView>(R.id.user_name)
        val userEmailTextView = view.findViewById<TextView>(R.id.user_email)
        val userPhoneTextView = view.findViewById<TextView>(R.id.user_phone)
        val editProfileButton = view.findViewById<Button>(R.id.edit_profile)


        val dbHelper = DBHelper(context)
        val cursor = dbHelper.getUser(email)
        cursor!!.moveToFirst()


        val id = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.ID_COL))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.NAME_COL))
        val phone = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PHONE_COL))
        val password = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_COL))


        userNameTextView.text = name
        userEmailTextView.text = email
        userPhoneTextView.text = phone


        editProfileButton.setOnClickListener{
            val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(id, name, email, phone, password)
            view.findNavController().navigate(action)
        }
        return view
    }
}