package com.example.chessapp.model.figures

import com.example.chessapp.model.Coordinates
import com.example.chessapp.model.Position

open class Figure(private val isWhite: Boolean) {
    fun isWhite(): Boolean = isWhite

    open fun getAllowedSteps(
        board: Array<Array<Position>>,
        coordinates: Coordinates
    ): ArrayList<Coordinates> {
        val list = ArrayList<Coordinates>()
        for (i in 0..7)
            for (j in 0..7)
                list.add(Coordinates(i,j))
        return list
    }
}