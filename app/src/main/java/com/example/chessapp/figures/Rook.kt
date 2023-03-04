package com.example.chessapp.figures

import com.example.chessapp.Coordinates
import com.example.chessapp.Position

class Rook(isWhite: Boolean): Figure(isWhite) {
    override fun toString(): String {
        return "Rook"
    }

    override fun getAllowedSteps(
        board: Array<Array<Position>>,
        coordinates: Coordinates
    ): ArrayList<Coordinates> {
        val steps = ArrayList<Coordinates>()

        for (x in coordinates.getX() + 1 until 8) {
            val y = coordinates.getY()
            if (board[x][y].getFigure() == null)
                steps.add(Coordinates(x, y))
            else {
                if (board[x][y].getFigure()!!.isWhite() != this.isWhite())
                    steps.add(Coordinates(x, y))
                break
            }
        }

        for (y in coordinates.getY() - 1 downTo 0) {
            val x = coordinates.getX()
            if (board[x][y].getFigure() == null)
                steps.add(Coordinates(x, y))
            else {
                if (board[x][y].getFigure()!!.isWhite() != this.isWhite())
                    steps.add(Coordinates(x, y))
                break
            }
        }

        for (x in coordinates.getX() - 1 downTo 0) {
            val y = coordinates.getY()
            if (board[x][y].getFigure() == null)
                steps.add(Coordinates(x, y))
            else {
                if (board[x][y].getFigure()!!.isWhite() != this.isWhite())
                    steps.add(Coordinates(x, y))
                break
            }
        }

        for (y in coordinates.getY() + 1 until 8) {
            val x = coordinates.getX()
            if (board[x][y].getFigure() == null)
                steps.add(Coordinates(x, y))
            else {
                if (board[x][y].getFigure()!!.isWhite() != this.isWhite())
                    steps.add(Coordinates(x, y))
                break
            }
        }

        return steps
    }
}