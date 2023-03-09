package com.example.chessapp.model.figures

import com.example.chessapp.model.Coordinates
import com.example.chessapp.model.Position

class Queen(isWhite: Boolean): Figure(isWhite) {
    override fun toString(): String {
        return "Queen"
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