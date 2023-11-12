package com.example.myapplication2

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageButton
import com.example.solar_cells_v3.cellList

object helper {

    lateinit var cellList:cellList
    lateinit var buttons:ArrayList<ArrayList<ImageButton>>
    lateinit var circuit: circuit

    fun isInitialised() = ::buttons.isInitialized


}