package com.example.chessapp.figures

import com.example.chessapp.Coordinates
import com.example.chessapp.Position

class Bishop(isWhite: Boolean) : Figure(isWhite) {
    override fun toString(): String {
        return "Bishop"
    }

    override fun getAllowedSteps(
        board: Array<Array<Position>>,
        coordinates: Coordinates
    ): ArrayList<Coordinates> {
        val steps = ArrayList<Coordinates>()
        var x: Int
        var y: Int

        for (i in 1 until 8) {
            x = coordinates.getX() + i
            y = coordinates.getY() + i
            if ( x < 8 &&  y < 8) {
                if (board[x][y].getFigure() == null)
                    steps.add(Coordinates(x, y))
                else {
                    if (board[x][y].getFigure()!!.isWhite() != this.isWhite())
                        steps.add(Coordinates(x, y))
                    break
                }
            }
        }

        for (i in 1 until 8) {
            x = coordinates.getX() - i
            y = coordinates.getY() + i
            if ( x >= 0 &&  y < 8) {
                if (board[x][y].getFigure() == null)
                    steps.add(Coordinates(x, y))
                else {
                    if (board[x][y].getFigure()!!.isWhite() != this.isWhite())
                        steps.add(Coordinates(x, y))
                    break
                }
            }
        }

        for (i in 1 until 8) {
            x = coordinates.getX() + i
            y = coordinates.getY() - i
            if ( x < 8 && y >= 0) {
                if (board[x][y].getFigure() == null)
                    steps.add(Coordinates(x, y))
                else {
                    if (board[x][y].getFigure()!!.isWhite() != this.isWhite())
                        steps.add(Coordinates(x, y))
                    break
                }
            }
        }

        for (i in 1 until 8) {
            x = coordinates.getX() - i
            y = coordinates.getY() - i
            if ( x >= 0 &&  y >= 0) {
                if (board[x][y].getFigure() == null)
                    steps.add(Coordinates(x, y))
                else {
                    if (board[x][y].getFigure()!!.isWhite() != this.isWhite())
                        steps.add(Coordinates(x, y))
                    break
                }
            }
        }

        return steps
    }
}