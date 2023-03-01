package com.example.chessapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

class MenuFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val tv = view.findViewById<Button>(R.id.menu_newPlay)
        // Inflate the layout for this fragment
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}