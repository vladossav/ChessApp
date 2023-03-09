package com.example.chessapp.model

class Coordinates(private var x: Int, private var y: Int) {

    fun setX(xx: Int) {
        x = xx
    }

    fun setXY(xx: Int, yy: Int) {
        x = xx
        y = yy
    }

    fun setY(yy: Int) {
        y = yy
    }

    fun getX(): Int = x
    fun getY(): Int = y
}