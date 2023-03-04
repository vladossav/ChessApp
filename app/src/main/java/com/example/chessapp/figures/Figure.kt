package com.example.chessapp.figures

import com.example.chessapp.Coordinates
import com.example.chessapp.Position

open class Figure(private val isWhite: Boolean) {
    fun isWhite(): Boolean = isWhite

    open fun getAllowedSteps(board: Array<Array<Position>>, coordinates: Coordinates): ArrayList<Coordinates> {
        val list = ArrayList<Coordinates>()
        for (i in 0..7)
            for (j in 0..7)
                list.add(Coordinates(i,j))
        return list
    }
}