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

        if (this.isWhite() && coordinates.getX() == 4 && coordinates.getY() == 7 &&
            board[5][7].getFigure() == null && board[6][7].getFigure() == null &&
            board[7][7].getFigure() is Rook && board[7][7].getFigure()!!.isWhite())
            steps.add(Coordinates(6,7))

        if (!this.isWhite() && coordinates.getX() == 4 && coordinates.getY() == 0 &&
            board[5][0].getFigure() == null && board[6][0].getFigure() == null &&
            board[7][0].getFigure() is Rook && !board[7][0].getFigure()!!.isWhite())
            steps.add(Coordinates(6,0))

        if (this.isWhite() && coordinates.getX() == 4 && coordinates.getY() == 7
            && board[1][7].getFigure() == null && board[2][7].getFigure() == null &&
            board[3][7].getFigure() == null &&  board[0][7].getFigure() is Rook
            && board[0][7].getFigure()!!.isWhite())
            steps.add(Coordinates(2,7))

        if (!this.isWhite() && coordinates.getX() == 4 && coordinates.getY() == 0
            && board[1][0].getFigure() == null && board[2][0].getFigure() == null &&
            board[3][0].getFigure() == null &&  board[0][0].getFigure() is Rook
            && !board[0][0].getFigure()!!.isWhite())
            steps.add(Coordinates(2,0))

        return steps
    }
}