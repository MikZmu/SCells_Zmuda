package com.example.myapplication2

import com.example.scells_zmuda.adjustedCell
import com.example.solar_cells_v3.Matrix_Cell
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class line() {

    var cells:ArrayList<adjustedCell> = ArrayList()
    var sum:ArrayList<Double> = ArrayList()
    var normalisedY:ArrayList<Double> = ArrayList()
    var dataPoints:LineGraphSeries<DataPoint?> = LineGraphSeries()
    var maxX:Double = 0.0
    var maxY:Double = 0.0
    var gridX:ArrayList<Double> = ArrayList()
    var gridY:ArrayList<Double> = ArrayList()
    var nonZero:Boolean = false

    init {
        for(i in 0..74){
            cells.add(adjustedCell("zeros."))
            gridX.add(0.0)
            gridY.add(0.0)
            nonZero = false
        }
    }

    fun modify(startIndex:Int,endIndex:Int, name:String, temp:Double,illu:Double){
        for(i in startIndex..endIndex){
            this.cells.get(i).changeParams(name,illu, temp)
        }
        this.sum = normaliseSum()
        this.maxX = this.maxX()
        this.maxY = this.maxY()
        this.gridY = this.cells.get(shortestYindex()).yGridArray
        this.gridX = Matrix_Cell.gridX(sum,0.01, this.maxX)
        this.normalisedY = this.normaliseYFun()
        this.nonZero = nonZero()
    }

    fun maxX():Double{
        return this.sum.max()
    }

    fun maxY():Double{
        var max = 0.0
        for(i in this.cells){
            if(i.yGridArray.max() > max){
                max = i.normalizedYArray.max()
            }
        }
        return max
    }

    fun longestY():Int{
        var returnVal:Int = 0

        for(i in this.cells){
            if(i.yGridArray.size > returnVal){
                returnVal = i.yGridArray.size
            }
        }
        return returnVal

    }

    fun longestYindex():Int{
        var returnVal:Int = 0
        var returnIndex:Int = 0
        for(i in this.cells){
            if(i.normalisedXArray.size > returnVal){
                returnVal = i.normalisedXArray.size
                returnIndex = this.cells.indexOf(i)
            }
        }
        return returnIndex

    }

    fun shortestYindex():Int{
        var returnVal:Int = this.cells.get(0).yGridArray.size
        var returnIndex:Int = 0
        for(i in this.cells){
            if(i.normalisedXArray.size <= returnVal && !(i.name.equals("zeros."))){
                returnVal = i.normalisedXArray.size
                returnIndex = this.cells.indexOf(i)
            }
        }
        return returnIndex

    }

    fun longestX():Int{
        var returnVal:Int = 0
        for(i in this.cells){
            if(i.normalisedXArray.size > returnVal){
                returnVal = i.normalisedXArray.size
            }
        }
        return returnVal

    }

    fun longestIndex():Int{
        var returnIndex:Int = 0
        var returnVal:Int = 0
        for(i in this.cells){
            if(i.normalisedXArray.size > returnVal){
                returnVal = i.normalisedXArray.size
                returnIndex = this.cells.indexOf(i)
            }
        }
        return returnIndex
    }



    fun normaliseSum():ArrayList<Double>{
        var xArray:ArrayList<Double> = calculateSum()
        var yArray:ArrayList<Double> = this.cells.get(longestYindex()).yGridArray
        var smallestMax:Double = this.cells.get(shortestYindex()).yGridArray.max()

        for(i in yArray){
            if(i > smallestMax){
                xArray.removeAt(yArray.indexOf(i))
            }
        }

        return xArray
    }






    fun calculateSum():ArrayList<Double>{
        var sumArray:ArrayList<Double> = ArrayList()
        var longest = this.cells.get(longestYindex()).yGridArray.size
        var stopper = 0
        while (stopper<longest){
            sumArray.add(0.0)
            for(i in this.cells){
                if(!i.name.equals("zeros")){
                    sumArray[stopper] += i.normalisedXArray.getOrElse(i.normalisedXArray.size-stopper){0.0}
                } else {
                    sumArray[stopper] += 0.0
                }
            }
            stopper++
        }
        var rtl:ArrayList<Double> = ArrayList(sumArray.sorted())
        return rtl
    }

    fun normaliseYFun():ArrayList<Double>{
        var shortestIndex = this.shortestYindex()
        var returnArray:ArrayList<Double> = ArrayList()
        returnArray = Matrix_Cell.normaliseYFun(this.sum, this.gridX,gridY)
        returnArray.add(0.0)
        return returnArray
    }

    fun nonZero():Boolean{
        var returnValue:Boolean = false

        for(cell in this.cells){
            if(!(cell.name.equals("zeros."))){
                returnValue = true
            }
        }
        return returnValue
    }




}
