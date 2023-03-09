package com.example.chessapp.model.figures

import com.example.chessapp.model.Coordinates
import com.example.chessapp.model.Position

class Knight(isWhite: Boolean): Figure(isWhite) {
    override fun toString(): String {
        return "Knight"

    }

    override fun getAllowedSteps(
        board: Array<Array<Position>>,
        coordinates: Coordinates
    ): ArrayList<Coordinates> {
        val steps = ArrayList<Coordinates>()
        val x = coordinates.getX()
        val y = coordinates.getY()

        fun checkPosition(xx: Int, yy: Int) {
            if (xx in 0..7 && yy in 0..7) {
                if (board[xx][yy].getFigure() == null)
                    steps.add(Coordinates(xx, yy))
                else if (board[xx][yy].getFigure()!!.isWhite() != this.isWhite())
                    steps.add(Coordinates(xx, yy))
            }
        }

        checkPosition(x + 1, y + 2)
        checkPosition(x + 2, y + 1)
        checkPosition(x + 2, y - 1)
        checkPosition(x + 1, y - 2)
        checkPosition(x - 1, y - 2)
        checkPosition(x - 2, y - 1)
        checkPosition(x - 2, y + 1)
        checkPosition(x - 1, y + 2)

        return steps
    }
}