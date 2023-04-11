package com.coding_titans.uptodate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController

class EditProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val saveChangesButton = view.findViewById<Button>(R.id.save_changes)

        val args = EditProfileFragmentArgs.fromBundle(requireArguments())
        val id = args.id
        val name = args.name
        val email = args.email
        val password = args.password
        val phone = args.phone

        val nameInput = view.findViewById<EditText>(R.id.input_your_name)
        val emailInput = view.findViewById<EditText>(R.id.input_email_address)
        val passwordInput = view.findViewById<EditText>(R.id.input_password)
        val phoneInput = view.findViewById<EditText>(R.id.input_phone_number)

        nameInput.setText(name)
        emailInput.setText(email)
        passwordInput.setText(password)
        phoneInput.setText(phone)

        saveChangesButton.setOnClickListener {
            val updatedName = nameInput.text.toString()
            val updatedEmail = emailInput.text.toString()
            val updatedPassword = passwordInput.text.toString()
            val updatedPhone = phoneInput.text.toString()

            if(updatedName.isEmpty() || updatedEmail.isEmpty() || updatedPassword.isEmpty() || updatedPhone.isEmpty()) {
                Toast.makeText(context, "Fields cannot be empty.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // update the name and grade using DBHelper
            val dbHelper = DBHelper(context)
            dbHelper.updateUserInfo(id.toString(),updatedName,updatedEmail,updatedPassword, updatedPhone)

            // navigate back to the main screen
            val action=EditProfileFragmentDirections.actionEditProfileFragmentToProfileFragment(email)
            view.findNavController().navigate(action)

        }
        return view
    }
}