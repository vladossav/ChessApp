package com.example.chessapp

import com.example.chessapp.figures.Figure

class Position(private var figure: Figure?) {

    fun getFigure(): Figure? {
        return figure
    }
    
    fun setFigure(inputFigure: Figure?) {
        figure = inputFigure
    }
}