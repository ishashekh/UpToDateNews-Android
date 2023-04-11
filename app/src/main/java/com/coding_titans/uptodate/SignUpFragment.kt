package com.coding_titans.uptodate

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController


class SignUpFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val dbHelper = DBHelper(context)

        val signUpBtn = view.findViewById<Button>(R.id.sign_up)
        val signInBtn = view.findViewById<Button>(R.id.sign_in_btn)

        val nameInput = view.findViewById<EditText>(R.id.input_your_name)
        val emailInput = view.findViewById<EditText>(R.id.input_email_address)
        val passwordInput = view.findViewById<EditText>(R.id.input_password)
        val phoneInput = view.findViewById<EditText>(R.id.input_phone_number)
        val termsCheck = view.findViewById<CheckBox>(R.id.check_terms)

        termsCheck.setOnClickListener {
            signUpBtn.isEnabled = termsCheck.isChecked
        }

        signInBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        signUpBtn.setOnClickListener {
            val name = nameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val phone = phoneInput.text.toString()

            when {
                name.isBlank() -> {
                    Toast.makeText(context, "Name cannot be blank!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                email.isBlank() || !email.contains("@") -> {
                    Toast.makeText(context, "Email is invalid!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                password.length < 6 -> {
                    Toast.makeText(context, "Password should be at least 6 characters long!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                phone.isBlank() || !phone.matches(Regex("[0-9]+")) -> {
                    Toast.makeText(context, "Phone number is invalid!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val isInserted = dbHelper.addNewUser(name, email, password, phone)

            when (isInserted) {
                0 -> {
                    Toast.makeText(context, "User already exists!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                -1 -> {
                    Toast.makeText(context, "Some error occurred! Try again in sometime.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            dbHelper.setLoggedIn(context as Activity, email)
            val action=SignUpFragmentDirections.actionSignUpFragmentToProfileFragment(email)
            view.findNavController().navigate(action)
        }
        return view
    }
}