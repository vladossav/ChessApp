package com.example.chessapp.screen

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.chessapp.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)
        val newGameBtn = view.findViewById<MaterialButton>(R.id.menu_newPlay)
        val exitBtn = view.findViewById<MaterialButton>(R.id.menu_exit)
        newGameBtn.setOnClickListener {
            showDialogToPlayNewGame()
        }
        exitBtn.setOnClickListener {
            requireActivity().finish()
        }

        return view
    }

    private fun showDialogToPlayNewGame() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_new_game)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnConnect: MaterialButton = dialog.findViewById(R.id.connect_btn)
        val btnCancel: MaterialButton = dialog.findViewById(R.id.cancel_connect_btn)
        val inputText: TextInputEditText = dialog.findViewById(R.id.input_txt)
        btnConnect.setOnClickListener {
            dialog.cancel()
            val text = inputText.text.toString()
            findNavController().navigate(
                R.id.action_menuFragment_to_chessboardFragment,
            bundleOf(ChessboardFragment.YOU_NAME_KEY to text))
        }
        btnCancel.setOnClickListener{
            dialog.cancel()
        }
        dialog.show()
    }
}