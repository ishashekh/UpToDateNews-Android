package com.coding_titans.uptodate

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController

class SignInFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        val username = view.findViewById<EditText>(R.id.input_your_name).text.toString()
        val password = view.findViewById<EditText>(R.id.input_email_address).text.toString()

        val signInBtn = view.findViewById<Button>(R.id.sign_in)
        val signUpBtn = view.findViewById<Button>(R.id.sign_up_btn)


        signUpBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        signInBtn.setOnClickListener {
            val username = view.findViewById<EditText>(R.id.input_your_name).text.toString()
            val password = view.findViewById<EditText>(R.id.input_email_address).text.toString()

            Log.v("email", username)
            Log.v("password", password)


            if(username.isEmpty() || password.isEmpty()) {
                val t = Toast(context)
                t.setText("Please fill both username and password")
                t.duration = Toast.LENGTH_SHORT
                t.show()
                return@setOnClickListener
            }

            val dbHelper = DBHelper(context)
            // TODO: check if user exists and then login
            val isUser = dbHelper.checkUser(username, password)

            if(isUser == 0) {
                val t = Toast(context)
                t.setText("Username or Password is incorrect")
                t.duration = Toast.LENGTH_SHORT
                t.show()
                return@setOnClickListener
            }

            dbHelper.setLoggedIn(context as Activity, username)

            // TODO: pass the email of logged in user to profile fragment
            val action = SignInFragmentDirections.actionSignInFragmentToProfileFragment(username)
            view.findNavController().navigate(action)
        }
        return view
    }
}