package com.example.chessapp.screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chessapp.R

class ChessboardFragment : Fragment() {
    private val vm: ChessboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val str = requireArguments().getString(YOU_NAME_KEY).toString()
        vm.yourName = str
        vm.socket.connect()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chessboard, container, false)

        for (i in 0..7)
            for (j in 0..7)
                vm.displayBoard[i][j] = view.findViewById(arrayOfCellIds[i][j])

        val grid = view.findViewById<GridLayout>(R.id.gridLayout)
        for (i in 0 until grid.childCount) {
            val tv: View = grid.getChildAt(i) as View
            tv.setOnClickListener {
                vm.cellHandling(it.id)
            }
        }

        val str = requireArguments().getString(YOU_NAME_KEY)
        view.findViewById<TextView>(R.id.you_tv).append(str)

        return view
    }

    companion object {
        const val YOU_NAME_KEY = "YOU_NAME_KEY"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.isLoading.observe(viewLifecycleOwner) {
            view.findViewById<ProgressBar>(R.id.loading).isVisible = it
            view.findViewById<View>(R.id.background_loading).isVisible = it
            view.findViewById<TextView>(R.id.loading_waiting_text).isVisible = it
        }

        vm.opponentName.observe(viewLifecycleOwner) {
            view.findViewById<TextView>(R.id.opponent_tv).append(it)
        }

        val stepTurnTv = view.findViewById<TextView>(R.id.step_tv)
        vm.whitePlayerTurn.observe(viewLifecycleOwner) {
            if (it) stepTurnTv.text = "?????? ??????????"
            else stepTurnTv.text = "?????? ????????????"
        }

        vm.gameFinished.observe(viewLifecycleOwner) {
            if (vm.gameFinished.value!!) {
                val msg = if (vm.whitePlayerTurn.value!!) "???????????? ????????????????!"
                else "?????????? ????????????????!"
                Log.d("board","CHECKMATE!!!")
                AlertDialog.Builder(requireContext())
                    .setTitle("???????? ??????????????????!")
                    .setMessage(msg)
                    .setPositiveButton("????") { _, _ ->
                        vm.socket.disconnect()
                        findNavController().popBackStack()
                    }
                    .setCancelable(false)
                    .show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(requireContext())
                        .setTitle("????????????????????????????!")
                        .setMessage("?????? ?????????? ?????????????????? ???????????????????????????? ??????????????????. ???? ??????????????, ?????? ???????????? ???????????")
                        .setPositiveButton("????") { _, _ ->
                            vm.socket.disconnect()
                            findNavController().popBackStack()
                        }
                        .setNegativeButton("????????????") {_,_ -> }
                        .show()
                }
            })
    }

    override fun onDestroy() {
        vm.socket.disconnect()
        super.onDestroy()
    }

    private val arrayOfCellIds = arrayOf(
        arrayOf(R.id.R00, R.id.R01, R.id.R02, R.id.R03, R.id.R04, R.id.R05, R.id.R06, R.id.R07),
        arrayOf(R.id.R10, R.id.R11, R.id.R12, R.id.R13, R.id.R14, R.id.R15, R.id.R16, R.id.R17),
        arrayOf(R.id.R20, R.id.R21, R.id.R22, R.id.R23, R.id.R24, R.id.R25, R.id.R26, R.id.R27),
        arrayOf(R.id.R30, R.id.R31, R.id.R32, R.id.R33, R.id.R34, R.id.R35, R.id.R36, R.id.R37),
        arrayOf(R.id.R40, R.id.R41, R.id.R42, R.id.R43, R.id.R44, R.id.R45, R.id.R46, R.id.R47),
        arrayOf(R.id.R50, R.id.R51, R.id.R52, R.id.R53, R.id.R54, R.id.R55, R.id.R56, R.id.R57),
        arrayOf(R.id.R60, R.id.R61, R.id.R62, R.id.R63, R.id.R64, R.id.R65, R.id.R66, R.id.R67),
        arrayOf(R.id.R70, R.id.R71, R.id.R72, R.id.R73, R.id.R74, R.id.R75, R.id.R76, R.id.R77)
    )
}