package com.example.scells_zmuda

import androidx.lifecycle.ViewModel

class circuitFragmentViewModel : ViewModel() {

    private var tempMatrix:ArrayList<ArrayList<Double>> = initTemp()
    private var illuMatrix:ArrayList<ArrayList<Double>> = initIllu()
    private var illuCol:ArrayList<ArrayList<Int>> = normaliseIllu()
    private var tempCol:ArrayList<ArrayList<Int>> = normaliseTemp()
    private var cellCol:ArrayList<ArrayList<String>> = initCells()
    private var toggle:Boolean = true
    private var counter:Int = 1
    private var firstX:Int = 0
    private var firstY:Int = 0
    private var secondX:Int = 0
    private var secondY:Int = 0

    fun getFX(): Int {
        return this.firstX
    }

    fun getFY(): Int {
        return this.firstY
    }

    fun getSX(): Int {
        return this.secondX
    }

    fun getSY(): Int {
        return this.secondY
    }

    fun setFX(fx:Int){
        this.firstX = fx
    }

    fun setFY(fy:Int){
        this.firstY = fy
    }

    fun setSX(sx:Int){
        this.secondX = sx
    }

    fun setSY(sy:Int){
        this.secondY = sy
    }


    fun getCnter(): Int {
        return this.counter
    }

    fun incCnter(){
        this.counter++
    }

    fun resCnter(){
        this.counter=0
    }


    fun initIllu(): ArrayList<ArrayList<Double>> {
        var returnArray:ArrayList<ArrayList<Double>> = ArrayList()
        var c = 0
        var d = 0

        while (c < 9){
            returnArray.add(ArrayList())
            d=0
            while (d < 25){
                returnArray[c].add(1000.0)
                d++
            }
            c++
        }
        return returnArray

    }

    fun initTemp(): ArrayList<ArrayList<Double>> {
        var returnArray:ArrayList<ArrayList<Double>> = ArrayList()
        var c = 0
        var d = 0

        while (c < 9){
            returnArray.add(ArrayList())
            d=0
            while (d < 25){
                returnArray[c].add(25.0)
                d++
            }
            c++
        }
        return returnArray

    }

    fun initCells(): ArrayList<ArrayList<String>> {
        var returnArray:ArrayList<ArrayList<String>> = ArrayList()
        var c = 0
        var d = 0

        while (c < 9){
            returnArray.add(ArrayList())
            d=0
            while (d < 25){
                returnArray[c].add("zeros.")
                d++
            }
            c++
        }
        return returnArray

    }

    fun changeData(firstX:Int, secondX:Int,firstY:Int,secondY:Int,illu:Double,temp:Double,name:String){

        var a:Int=0
        var b:Int=0
        var c:Int=0
        var d:Int=0

        if(firstX >= secondX){
            a = secondX
            b = firstX
        }
        if(secondX > firstX){
            a = firstX
            b = secondX
        }

        if(firstY >= secondY){
            c  = secondY
            d = firstY
        }
        if(firstY<secondY){
            c = firstY
            d = secondY
        }
        for(i in a..b){
            var rowTemp = this.tempMatrix.get(i)
            var rowIllu = this.illuMatrix.get(i)
            var rowCells = this.cellCol.get(i)
            for(j in c..d){
                rowTemp[j] = temp
                rowIllu[j] = illu
                rowCells[j] = name
            }
        }
        this.tempCol = normaliseTemp()
        this.illuCol = normaliseIllu()


    }

    fun getTemp(): ArrayList<ArrayList<Int>> {
        return this.tempCol
    }

    fun getIllu():ArrayList<ArrayList<Int>>{
        return this.illuCol
    }

    fun getToggle():Boolean{
        return this.toggle
    }

    fun normaliseIllu():ArrayList<ArrayList<Int>>{
        val returnList:ArrayList<ArrayList<Int>> = ArrayList()
        var i = 0
        while (i < this.illuMatrix.size){
            returnList.add(ArrayList())
            val row = this.illuMatrix.get(i)
            var j = 0
            while(j < row.size){
                returnList[i].add(((illuMatrix[i][j]-150)/1500*255).toInt())
                j++
            }
            i++
        }
        return returnList
    }

    fun getCells(): ArrayList<ArrayList<String>> {
        return this.cellCol
    }

    fun normaliseTemp():ArrayList<ArrayList<Int>>{
        var returnList:ArrayList<ArrayList<Int>> = ArrayList()
        var i = 0
        while (i < this.tempMatrix.size){
            returnList.add(ArrayList())
            var row = this.tempMatrix.get(i)
            var j = 0
            while(j < row.size){
                returnList[i].add((((tempMatrix[i][j])+25)/150*255).toInt())
                j++
            }
            i++
        }

        return returnList
    }

    fun toggleToggle(){
        if(this.toggle == true){
            this.toggle = false
        } else{
            this.toggle = true
        }
    }


}