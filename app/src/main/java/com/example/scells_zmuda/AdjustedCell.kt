package com.example.scells_zmuda

import com.example.myapplication2.Singleton
import com.example.solar_cells_v3.MatrixCell

class AdjustedCell(
    var name:String,
    var temp:Double = Singleton.cellList.getCell(name).startingTemp,
    var Illu:Double = Singleton.cellList.getCell(name).startingIllu,
    var xArray:ArrayList<Double> = Singleton.cellList.getCell(name).vArray,
    var yArray:ArrayList<Double> = Singleton.cellList.getCell(name).iArray,
    var xGridArray:ArrayList<Double> = MatrixCell.gridX(0.01, xArray.max()),
    var yGridArray: ArrayList<Double> = MatrixCell.gridY(0.01, yArray.max()),
    var perfectXArray:ArrayList<Double> = ArrayList(),
    var normalisedXArray:ArrayList<Double> = ArrayList()
    )

{
    fun changeParams(name: String, illu:Double, temp:Double){
        this.name = name
        this.Illu = illu
        this.temp = temp
        val startingTemp = Singleton.cellList.getCell(name).startingTemp
        val i_illu_coe = Singleton.cellList.getCell(name).iIlluCoe
        val i_temp_coe = Singleton.cellList.getCell(name).iTempCoe
        val v_temp_coe = Singleton.cellList.getCell(name).vTempCoe
        val startingIllu = Singleton.cellList.getCell(name).startingIllu
        this.xArray = Singleton.cellList.getCell(name).vArray
        this.yArray = Singleton.cellList.getCell(name).iArray
        val adjustedPair = MatrixCell.adjustForParams(xArray,yArray, startingTemp, v_temp_coe, i_temp_coe, temp, startingIllu, i_illu_coe, illu)
        this.xArray = adjustedPair.first
        this.yArray = adjustedPair.second
        xGridArray = MatrixCell.gridX(MatrixCell.stepCalc(this.xArray[this.xArray.size-1]), this.xArray[this.xArray.size-1])
    }

    fun updateNormX(step:Double, value:Double){
        this.normalisedXArray = MatrixCell.normaliseXFun(yArray,MatrixCell.gridY(step, this.yArray[0]),xArray)
    }
    fun updatePrfktX(step:Double, value:Double){
        this.perfectXArray = MatrixCell.normaliseXFun(yArray,MatrixCell.gridY(step, this.yArray[0]),xArray)
    }







}