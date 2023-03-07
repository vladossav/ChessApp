package com.example.chessapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.chessapp.figures.*
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlin.collections.ArrayList


class ChessboardFragment : Fragment() {
    private var displayBoard = Array(8) {Array<TextView?>(8) {null} }
    private var boardFigures = Array(8) {Array(8) {Position(null)} }
    private var arrayOfCellIds = arrayOf(
        arrayOf(R.id.R00, R.id.R01, R.id.R02, R.id.R03, R.id.R04, R.id.R05, R.id.R06, R.id.R07),
        arrayOf(R.id.R10, R.id.R11, R.id.R12, R.id.R13, R.id.R14, R.id.R15, R.id.R16, R.id.R17),
        arrayOf(R.id.R20, R.id.R21, R.id.R22, R.id.R23, R.id.R24, R.id.R25, R.id.R26, R.id.R27),
        arrayOf(R.id.R30, R.id.R31, R.id.R32, R.id.R33, R.id.R34, R.id.R35, R.id.R36, R.id.R37),
        arrayOf(R.id.R40, R.id.R41, R.id.R42, R.id.R43, R.id.R44, R.id.R45, R.id.R46, R.id.R47),
        arrayOf(R.id.R50, R.id.R51, R.id.R52, R.id.R53, R.id.R54, R.id.R55, R.id.R56, R.id.R57),
        arrayOf(R.id.R60, R.id.R61, R.id.R62, R.id.R63, R.id.R64, R.id.R65, R.id.R66, R.id.R67),
        arrayOf(R.id.R70, R.id.R71, R.id.R72, R.id.R73, R.id.R74, R.id.R75, R.id.R76, R.id.R77)
    )
    private var clickedPos: Coordinates = Coordinates(0,0)
    private var lastPos : Coordinates = Coordinates(0,0)
    private var cellSelected = false
    private var whitePlayerTurn = true
    private var whiteChessboardSide = true
    private var numOfMoves = 0
    private var listOfAllowedSteps = ArrayList<Coordinates>()
    lateinit var socket: Socket

    val viewModel: ChessboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            //socket = IO.socket("http://37.194.20.87:8000")
            socket = IO.socket("http://localhost:8000")

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("socket", "Failed to connect")
        }

        socket.connect()

        socket.on(Socket.EVENT_CONNECT, onConnect)

    }

    var onConnect = Emitter.Listener {
        Log.d("socket", "socketID: " + socket.id())
        Log.d("socket","success")
        Log.d("socket",socket.connected().toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chessboard, container, false)

        for (i in 0..7)
            for (j in 0..7)
                displayBoard[i][j] = view.findViewById(arrayOfCellIds[i][j])

        val grid = view.findViewById<GridLayout>(R.id.gridLayout)
        for (i in 0 until grid.childCount) {
            val tv: TextView = grid.getChildAt(i) as TextView
            tv.setOnClickListener {
                onCellClick(it)
            }
        }

        initBoard()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Предупреждение!")
                        .setMessage("Вам будет присвоено автоматическое поражение. Вы уверены, что хотите выйти?")
                        .setPositiveButton("Да") { _, _ ->
                           findNavController().popBackStack()
                        }
                        .setNegativeButton("Отмена") {_,_ -> }
                        .show()
                }
            })
    }

    private fun initBoard() {
        boardFigures[0][0].setFigure(Rook(false))
        boardFigures[1][0].setFigure(Knight(false))
        boardFigures[2][0].setFigure(Bishop(false))
        boardFigures[3][0].setFigure(Queen(false))
        boardFigures[4][0].setFigure(King(false))
        boardFigures[5][0].setFigure(Bishop(false))
        boardFigures[6][0].setFigure(Knight(false))
        boardFigures[7][0].setFigure(Rook(false))

        boardFigures[0][7].setFigure(Rook(true))
        boardFigures[1][7].setFigure(Knight(true))
        boardFigures[2][7].setFigure(Bishop(true))
        boardFigures[3][7].setFigure(Queen(true))
        boardFigures[4][7].setFigure(King(true))
        boardFigures[5][7].setFigure(Bishop(true))
        boardFigures[6][7].setFigure(Knight(true))
        boardFigures[7][7].setFigure(Rook(true))

        for (i in 0..7) {
            boardFigures[i][1].setFigure(Pawn(false))
            boardFigures[i][6].setFigure(Pawn(true))
        }

        cellSelected = false
        numOfMoves = 0
        setBoard()
    }


    private fun setBoard() {
        for (i in 0..7)
            for (j in 0..7) {
                val p: Figure? = boardFigures[i][j].getFigure()

                if (p != null) {
                    if (p is Pawn) {
                        if (p.isWhite()) displayBoard[i][j]?.setBackgroundResource(R.drawable.wpawn)
                        else displayBoard[i][j]?.setBackgroundResource(R.drawable.bpawn)
                    }
                    else if (p is Queen) {
                        if (p.isWhite()) displayBoard[i][j]?.setBackgroundResource(R.drawable.wqueen)
                        else displayBoard[i][j]?.setBackgroundResource(R.drawable.bqueen)
                    }
                    else if (p is Knight) {
                        if (p.isWhite()) displayBoard[i][j]?.setBackgroundResource(R.drawable.wknight)
                        else displayBoard[i][j]?.setBackgroundResource(R.drawable.bknight)
                    }
                    else if (p is Bishop) {
                        if (p.isWhite()) displayBoard[i][j]?.setBackgroundResource(R.drawable.wbishop)
                        else displayBoard[i][j]?.setBackgroundResource(R.drawable.bbishop)
                    }
                    else if (p is King) {
                        if (p.isWhite()) displayBoard[i][j]?.setBackgroundResource(R.drawable.wking)
                        else displayBoard[i][j]?.setBackgroundResource(R.drawable.bking)
                    }
                    else if (p is Rook) {
                        if (p.isWhite()) displayBoard[i][j]?.setBackgroundResource(R.drawable.wrook)
                        else displayBoard[i][j]?.setBackgroundResource(R.drawable.brook)
                    }
                }
                else {
                    displayBoard[i][j]?.setBackgroundResource(0)
                }
            }
    }

    override fun onDestroy() {
        socket.disconnect()
        super.onDestroy()
    }

    private fun onCellClick(view: View) {
        when(view.id) {
            R.id.R00 -> clickedPos.setXY(0,0)
            R.id.R10 -> clickedPos.setXY(1,0)
            R.id.R20 -> clickedPos.setXY(2,0)
            R.id.R30 -> clickedPos.setXY(3,0)
            R.id.R40 -> clickedPos.setXY(4,0)
            R.id.R50 -> clickedPos.setXY(5,0)
            R.id.R60 -> clickedPos.setXY(6,0)
            R.id.R70 -> clickedPos.setXY(7,0)

            R.id.R01 -> clickedPos.setXY(0,1)
            R.id.R11 -> clickedPos.setXY(1,1)
            R.id.R21 -> clickedPos.setXY(2,1)
            R.id.R31 -> clickedPos.setXY(3,1)
            R.id.R41 -> clickedPos.setXY(4,1)
            R.id.R51 -> clickedPos.setXY(5,1)
            R.id.R61 -> clickedPos.setXY(6,1)
            R.id.R71 -> clickedPos.setXY(7,1)

            R.id.R02 -> clickedPos.setXY(0,2)
            R.id.R12 -> clickedPos.setXY(1,2)
            R.id.R22 -> clickedPos.setXY(2,2)
            R.id.R32 -> clickedPos.setXY(3,2)
            R.id.R42 -> clickedPos.setXY(4,2)
            R.id.R52 -> clickedPos.setXY(5,2)
            R.id.R62 -> clickedPos.setXY(6,2)
            R.id.R72 -> clickedPos.setXY(7,2)

            R.id.R03 -> clickedPos.setXY(0,3)
            R.id.R13 -> clickedPos.setXY(1,3)
            R.id.R23 -> clickedPos.setXY(2,3)
            R.id.R33 -> clickedPos.setXY(3,3)
            R.id.R43 -> clickedPos.setXY(4,3)
            R.id.R53 -> clickedPos.setXY(5,3)
            R.id.R63 -> clickedPos.setXY(6,3)
            R.id.R73 -> clickedPos.setXY(7,3)

            R.id.R04 -> clickedPos.setXY(0,4)
            R.id.R14 -> clickedPos.setXY(1,4)
            R.id.R24 -> clickedPos.setXY(2,4)
            R.id.R34 -> clickedPos.setXY(3,4)
            R.id.R44 -> clickedPos.setXY(4,4)
            R.id.R54 -> clickedPos.setXY(5,4)
            R.id.R64 -> clickedPos.setXY(6,4)
            R.id.R74 -> clickedPos.setXY(7,4)

            R.id.R05 -> clickedPos.setXY(0,5)
            R.id.R15 -> clickedPos.setXY(1,5)
            R.id.R25 -> clickedPos.setXY(2,5)
            R.id.R35 -> clickedPos.setXY(3,5)
            R.id.R45 -> clickedPos.setXY(4,5)
            R.id.R55 -> clickedPos.setXY(5,5)
            R.id.R65 -> clickedPos.setXY(6,5)
            R.id.R75 -> clickedPos.setXY(7,5)

            R.id.R06 -> clickedPos.setXY(0,6)
            R.id.R16 -> clickedPos.setXY(1,6)
            R.id.R26 -> clickedPos.setXY(2,6)
            R.id.R36 -> clickedPos.setXY(3,6)
            R.id.R46 -> clickedPos.setXY(4,6)
            R.id.R56 -> clickedPos.setXY(5,6)
            R.id.R66 -> clickedPos.setXY(6,6)
            R.id.R76 -> clickedPos.setXY(7,6)

            R.id.R07 -> clickedPos.setXY(0,7)
            R.id.R17 -> clickedPos.setXY(1,7)
            R.id.R27 -> clickedPos.setXY(2,7)
            R.id.R37 -> clickedPos.setXY(3,7)
            R.id.R47 -> clickedPos.setXY(4,7)
            R.id.R57 -> clickedPos.setXY(5,7)
            R.id.R67 -> clickedPos.setXY(6,7)
            R.id.R77 -> clickedPos.setXY(7,7)
            else -> Log.d("cell","wrong")
        }

        val x = clickedPos.getX()
        val y = clickedPos.getY()
        Log.d("cell", "$x $y")

        if (!cellSelected) {
            if (boardFigures[x][y].getFigure() == null)
                return
            else {
                if (boardFigures[x][y].getFigure()!!.isWhite() != whitePlayerTurn)
                    return
                listOfAllowedSteps.clear()
                listOfAllowedSteps = boardFigures[x][y].getFigure()!!.getAllowedSteps(boardFigures, clickedPos)
                cellSelected = true
            }
        }
        else {

            if (checkStepIsAllowed(clickedPos)) {
                val attackedFigure = boardFigures[x][y].getFigure()

                boardFigures[x][y].setFigure(boardFigures[lastPos.getX()][lastPos.getY()].getFigure())
                boardFigures[lastPos.getX()][lastPos.getY()].setFigure(null)
                if (isKingInDanger(boardFigures)) {

                    boardFigures[lastPos.getX()][lastPos.getY()].setFigure(boardFigures[x][y].getFigure())
                    boardFigures[x][y].setFigure(attackedFigure)
                    cellSelected = false
                    return
                }

                //проверка рокировки
                if (boardFigures[x][y].getFigure() is King && x == (lastPos.getX() + 2)) {
                    boardFigures[5][y].setFigure(boardFigures[7][y].getFigure())
                    boardFigures[7][y].setFigure(null)
                }
                if (boardFigures[x][y].getFigure() is King && x == (lastPos.getX() - 2)) {
                    boardFigures[3][y].setFigure(boardFigures[0][y].getFigure())
                    boardFigures[0][y].setFigure(null)
                }

                whitePlayerTurn = !whitePlayerTurn
            }
            cellSelected = false
        }

        lastPos = Coordinates(x, y)
        setBoard()

        if (isKingInDanger(boardFigures) && isCheckmate()) {

            Log.d("board","CHECKMATE!!!")
            AlertDialog.Builder(requireContext())
                .setTitle("Игра закончена!")
                .setMessage("_____ выиграли!")
                .setPositiveButton("ОК") { _, _ ->
                    findNavController().popBackStack()
                }
                .show()
        }
    }

    private fun checkStepIsAllowed(step: Coordinates): Boolean {
        for (pos in listOfAllowedSteps)
            if (pos.getX() == step.getX() && pos.getY() == step.getY())
                return true
        return false
    }

    private fun isKingInDanger(board: Array<Array<Position>>):Boolean {
        var kingPos: Coordinates = Coordinates(0,0)
        val listOfEnemyFigures = ArrayList<Coordinates>()
        for (i in 0..7)
            for (j in 0..7)
                if (board[i][j].getFigure() != null) {
                    if (board[i][j].getFigure()!!.isWhite() != whitePlayerTurn) {
                        listOfEnemyFigures.add(Coordinates(i,j))
                        continue
                    }
                    if (board[i][j].getFigure() is King &&
                        board[i][j].getFigure()!!.isWhite() == whitePlayerTurn) {
                        kingPos = Coordinates(i,j)
                    }
                }

        for (pos in listOfEnemyFigures) {
            val figure = board[pos.getX()][pos.getY()].getFigure()
            var list: ArrayList<Coordinates>

            list = figure!!.getAllowedSteps(board, pos)
            if (figure is Pawn)
                list = figure.getStepsWhichCanAttack()

            for (allowedPos in list)
                if (allowedPos.getX() == kingPos.getX() && allowedPos.getY() == kingPos.getY())
                    return true
        }
        return false
    }

    fun isCheckmate(): Boolean {
        var kingPos: Coordinates = Coordinates(2,2)
        for (i in 0..7)
            for (j in 0..7)
                if (boardFigures[i][j].getFigure() != null) {
                    if (boardFigures[i][j].getFigure() is King &&
                        boardFigures[i][j].getFigure()!!.isWhite() == whitePlayerTurn) {
                        kingPos = Coordinates(i,j)
                    }
                }

        val xKing = kingPos.getX()
        val yKing = kingPos.getY()
        Log.d("board","$xKing $yKing")

        if (boardFigures[xKing][yKing].getFigure() !is King) return false
        val kingAllowedSteps = boardFigures[xKing][yKing].getFigure()!!
            .getAllowedSteps(boardFigures,kingPos)
        for (step in kingAllowedSteps) {
            val board: Array<Array<Position>> = Array(8) {Array(8) {Position(null)} }
            for (i in 0..7)
                for (j in 0..7)
                    board[i][j] = Position(boardFigures[i][j])

            board[step.getX()][step.getY()].setFigure(boardFigures[xKing][yKing].getFigure())
            if (!isKingInDanger(board)) return false

        }
        return true
    }

}