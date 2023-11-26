package com.example.scells_zmuda

import com.example.myapplication2.helper
import com.example.solar_cells_v3.Matrix_Cell
import com.example.solar_cells_v3.cellList

class adjustedCell(
    var name:String,
    var temp:Double = helper.cellList.getCell(name).startingTemp,
    var Illu:Double = helper.cellList.getCell(name).startingIllu,
    var xArray:ArrayList<Double> = helper.cellList.getCell(name).xArray,
    var yArray:ArrayList<Double> = helper.cellList.getCell(name).yArray,
    var xGridArray:ArrayList<Double> = Matrix_Cell.gridX(0.01, xArray.max()),
    var yGridArray: ArrayList<Double> = Matrix_Cell.gridY(0.01, yArray.max()),
    var normalizedYArray:ArrayList<Double> = Matrix_Cell.normaliseYFun(xArray, xGridArray, yArray),
    var normalisedXArray:ArrayList<Double> = Matrix_Cell.normaliseXFun(yArray, yGridArray, xArray),
    )

{
    fun changeParams(name: String, illu:Double, temp:Double){
        this.name = name
        this.Illu = illu
        this.temp = temp
        var startingTemp = helper.cellList.getCell(name).startingTemp
        var i_illu_coe = helper.cellList.getCell(name).i_illu_coe
        var i_temp_coe = helper.cellList.getCell(name).i_temp_coe
        var v_temp_coe = helper.cellList.getCell(name).v_temp_coe
        var startingIllu = helper.cellList.getCell(name).startingIllu
        this.xArray = helper.cellList.getCell(name).xArray
        this.yArray = helper.cellList.getCell(name).yArray
        var adjustedPair = Matrix_Cell.adjustForParams(xArray,yArray, startingTemp, v_temp_coe, i_temp_coe, temp, startingIllu, i_illu_coe, illu)
        xArray = adjustedPair.first
        yArray = adjustedPair.second
        xGridArray = Matrix_Cell.gridX(0.01, xArray.max())
        yGridArray = Matrix_Cell.gridY(0.01, yArray.max())
        normalizedYArray = Matrix_Cell.normaliseYFun(xArray, xGridArray, yArray)
        normalisedXArray = Matrix_Cell.normaliseXFun(yArray, yGridArray, xArray)

    }





}