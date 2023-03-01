package com.example.chessapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = ChessboardFragment.newInstance()
       supportFragmentManager
            .beginTransaction()
            .add(R.id.fragmentHolder, fragment)
            .commit()
    }
}