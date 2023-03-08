package com.example.chessapp

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chessapp.figures.*

class ChessboardViewModel: ViewModel() {
    var displayBoard = Array(8) {Array<View?>(8) {null} }
    var whitePlayerTurn: MutableLiveData<Boolean> = MutableLiveData(true)
    var gameFinished: MutableLiveData<Boolean> = MutableLiveData(false)
    var youName: String = ""
    var opponentName: String = ""

    private var boardFigures = Array(8) {Array(8) {Position(null)} }
    private var clickedPos: Coordinates = Coordinates(0,0)
    private var lastPos: Coordinates = Coordinates(0,0)
    private var cellSelected = false

    var isWhiteChessboardSide = true
    private var numOfMoves = 0
    private var listOfAllowedSteps = ArrayList<Coordinates>()

    fun initBoard(isWhiteSide: Boolean) {
        isWhiteChessboardSide = isWhiteSide
        setStartPosition()
        cellSelected = false
        numOfMoves = 0
        setBoard()
    }

    fun cellHandling(cellId: Int) {
        setClickPosition(cellId)

        val x = clickedPos.getX()
        val y = clickedPos.getY()
        Log.d("cell", "$x $y")

        if (!cellSelected) {
            if (boardFigures[x][y].getFigure() == null)
                return
            else {
                if (boardFigures[x][y].getFigure()!!.isWhite() != whitePlayerTurn.value)
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

                whitePlayerTurn.value = !whitePlayerTurn.value!!
            }
            cellSelected = false
        }

        lastPos = Coordinates(x, y)
        setBoard()

        if (isKingInDanger(boardFigures) && isCheckmate()) gameFinished.postValue(true)
    }

    private fun isKingInDanger(board: Array<Array<Position>>):Boolean {
        var kingPos: Coordinates = Coordinates(0,0)
        val listOfEnemyFigures = ArrayList<Coordinates>()
        for (i in 0..7)
            for (j in 0..7)
                if (board[i][j].getFigure() != null) {
                    if (board[i][j].getFigure()!!.isWhite() != whitePlayerTurn.value) {
                        listOfEnemyFigures.add(Coordinates(i,j))
                        continue
                    }
                    if (board[i][j].getFigure() is King &&
                        board[i][j].getFigure()!!.isWhite() == whitePlayerTurn.value) {
                        kingPos = Coordinates(i,j)
                    }
                }

        for (pos in listOfEnemyFigures) {
            val figure = board[pos.getX()][pos.getY()].getFigure()
            var list = figure!!.getAllowedSteps(board, pos)

            if (figure is Pawn)
                list = figure.getStepsWhichCanAttack()

            for (allowedPos in list)
                if (allowedPos.getX() == kingPos.getX() && allowedPos.getY() == kingPos.getY())
                    return true
        }
        return false
    }

    private fun isCheckmate(): Boolean {
        var kingPos: Coordinates = Coordinates(2,2)
        for (i in 0..7)
            for (j in 0..7)
                if (boardFigures[i][j].getFigure() != null) {
                    if (boardFigures[i][j].getFigure() is King &&
                        boardFigures[i][j].getFigure()!!.isWhite() == whitePlayerTurn.value) {
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

    private fun checkStepIsAllowed(step: Coordinates): Boolean {
        for (pos in listOfAllowedSteps)
            if (pos.getX() == step.getX() && pos.getY() == step.getY())
                return true
        return false
    }

    private fun setBoard() {
        for (i in 0..7)
            for (j in 0..7) {
                var x = i
                var y = j
                if (!isWhiteChessboardSide) {
                    x = 7 - i
                    y = 7 - j
                }
                val p: Figure? = boardFigures[i][j].getFigure()

                if (p != null) {
                    if (p is Pawn) {
                        if (p.isWhite()) displayBoard[x][y]?.setBackgroundResource(R.drawable.wpawn)
                        else displayBoard[x][y]?.setBackgroundResource(R.drawable.bpawn)
                    }
                    else if (p is Queen) {
                        if (p.isWhite()) displayBoard[x][y]?.setBackgroundResource(R.drawable.wqueen)
                        else displayBoard[x][y]?.setBackgroundResource(R.drawable.bqueen)
                    }
                    else if (p is Knight) {
                        if (p.isWhite()) displayBoard[x][y]?.setBackgroundResource(R.drawable.wknight)
                        else displayBoard[x][y]?.setBackgroundResource(R.drawable.bknight)
                    }
                    else if (p is Bishop) {
                        if (p.isWhite()) displayBoard[x][y]?.setBackgroundResource(R.drawable.wbishop)
                        else displayBoard[x][y]?.setBackgroundResource(R.drawable.bbishop)
                    }
                    else if (p is King) {
                        if (p.isWhite()) displayBoard[x][y]?.setBackgroundResource(R.drawable.wking)
                        else displayBoard[x][y]?.setBackgroundResource(R.drawable.bking)
                    }
                    else if (p is Rook) {
                        if (p.isWhite()) displayBoard[x][y]?.setBackgroundResource(R.drawable.wrook)
                        else displayBoard[x][y]?.setBackgroundResource(R.drawable.brook)
                    }
                }
                else {
                    displayBoard[x][y]?.setBackgroundResource(0)
                }
            }
    }

    private fun setStartPosition() {
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
    }

    private fun setClickPosition(cellId: Int) {
        when(cellId) {
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
        }
        if (!isWhiteChessboardSide) clickedPos.setXY(7 - clickedPos.getX(), 7 - clickedPos.getY())
    }
}