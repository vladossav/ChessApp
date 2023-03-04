package com.example.chessapp.figures

import com.example.chessapp.Coordinates
import com.example.chessapp.Position

class King(isWhite: Boolean): Figure(isWhite) {
    override fun toString(): String {
        return "King"
    }

    override fun getAllowedSteps(
        board: Array<Array<Position>>,
        coordinates: Coordinates
    ): ArrayList<Coordinates> {
        val steps = ArrayList<Coordinates>()

        for (x in coordinates.getX()-1..coordinates.getX()+1)
            for (y in coordinates.getY()-1..coordinates.getY()+1) {
                if (x in 0..7 && y in 0..7) {
                    if (x == coordinates.getX() && y == coordinates.getX()) continue
                    if (board[x][y].getFigure() == null)
                        steps.add(Coordinates(x, y))
                    else if (board[x][y].getFigure()!!.isWhite() != this.isWhite())
                        steps.add(Coordinates(x, y))
                }
            }

        return steps
    }
}