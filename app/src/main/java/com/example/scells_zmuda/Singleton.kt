package com.example.myapplication2

import android.widget.ImageButton
import com.example.solar_cells_v3.CellList

object Singleton {

    lateinit var cellList:CellList
    lateinit var buttons:ArrayList<ArrayList<ImageButton>>
    lateinit var circuit: Circuit

    fun isInitialised() = ::buttons.isInitialized


}