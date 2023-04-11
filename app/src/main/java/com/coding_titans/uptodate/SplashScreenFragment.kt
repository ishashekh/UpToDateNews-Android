package com.coding_titans.uptodate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController

class SplashScreenFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        val appLogo = view.findViewById<ImageView>(R.id.app_logo)

        appLogo.setOnClickListener{
            view.findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
        }

        return view
    }
}