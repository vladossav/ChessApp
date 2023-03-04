package com.example.chessapp.figures

import com.example.chessapp.Coordinates
import com.example.chessapp.Position

class Pawn(isWhite: Boolean): Figure(isWhite) {
    override fun toString(): String {
        return "Pawn"
    }

    override fun getAllowedSteps(
        board: Array<Array<Position>>,
        coordinates: Coordinates
    ): ArrayList<Coordinates> {
        val steps = ArrayList<Coordinates>()
        val x = coordinates.getX()
        val y = coordinates.getY()

        if (this.isWhite()) {
            if (y-1 >= 0) {
                if (board[x][y-1].getFigure() == null) {
                    steps.add(Coordinates(x,y-1))
                    if (y == 6 && board[x][y-2].getFigure() == null)
                        steps.add(Coordinates(x,y-2))
                }

                if (x+1 < 8) {
                    if (board[x+1][y-1].getFigure() != null &&
                        board[x+1][y-1].getFigure()!!.isWhite() != this.isWhite()) {
                        steps.add(Coordinates(x+1,y-1))
                    }
                }

                if (x-1 >= 0) {
                    if (board[x-1][y-1].getFigure() != null &&
                        board[x-1][y-1].getFigure()!!.isWhite() != this.isWhite()) {
                        steps.add(Coordinates(x-1,y-1))
                    }
                }
            }
        }
        else {
            if (y+1 < 8) {
                if (board[x][y+1].getFigure() == null) {
                    steps.add(Coordinates(x,y+1))
                    if (y == 1 && board[x][y+2].getFigure() == null)
                        steps.add(Coordinates(x,y+2))
                }

                if (x+1 < 8) {
                    if (board[x+1][y+1].getFigure() != null &&
                        board[x+1][y+1].getFigure()!!.isWhite() != this.isWhite()) {
                        steps.add(Coordinates(x+1,y+1))
                    }
                }

                if (x-1 >= 0) {
                    if (board[x-1][y+1].getFigure() != null &&
                        board[x-1][y+1].getFigure()!!.isWhite() != this.isWhite()) {
                        steps.add(Coordinates(x-1,y+1))
                    }
                }
            }
        }

        return steps
    }
}