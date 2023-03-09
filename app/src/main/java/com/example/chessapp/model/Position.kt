package com.example.chessapp.model

import com.example.chessapp.model.figures.Figure

class Position(private var figure: Figure?) {
    constructor(pos: Position) : this(pos.figure)

    fun getFigure(): Figure? {
        return figure
    }
    
    fun setFigure(inputFigure: Figure?) {
        figure = inputFigure
    }
}