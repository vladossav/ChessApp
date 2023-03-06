package com.example.chessapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class MenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val newGameBtn = view.findViewById<Button>(R.id.menu_newPlay)
        val exitBtn = view.findViewById<Button>(R.id.menu_exit)
        newGameBtn.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_chessboardFragment)
        }
        exitBtn.setOnClickListener {
            requireActivity().finish()
        }

        return view
    }

}