package com.coding_titans.uptodate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class AboutUsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)

        // Define all the views
        val inputName = view.findViewById<TextView>(R.id.input_your_name)
        val inputEmail = view.findViewById<TextView>(R.id.input_email_address)
        val inputMessage = view.findViewById<EditText>(R.id.input_message)
        val submitButton = view.findViewById<Button>(R.id.saveButton)

        // Set click listener for submit button
        submitButton.setOnClickListener {
            // Validate inputs
            if (validateInputs(inputName, inputEmail, inputMessage)) {
                // Display confirmation message
                Toast.makeText(context, "Email sent successfully.", Toast.LENGTH_SHORT).show()

                // Clear form data
                clearForm(inputName, inputEmail, inputMessage)
            } else {
                Toast.makeText(context, "Please enter valid inputs.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    // Function to validate inputs
    private fun validateInputs(
        inputName: TextView, inputEmail: TextView,
        inputMessage: EditText
    ): Boolean {

        // Validate name
        val name = inputName.text.toString()
        if (name.isEmpty()) {
            return false
        }

        // Validate email
        val email = inputEmail.text.toString()
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }

        // Validate message
        val message = inputMessage.text.toString()
        if (message.isEmpty()) {
            return false
        }

        return true
    }

    // Function to clear form data
    private fun clearForm(inputName: TextView, inputEmail: TextView, inputMessage: EditText) {
        inputName.text = ""
        inputEmail.text = ""
        inputMessage.text = null
    }
}
