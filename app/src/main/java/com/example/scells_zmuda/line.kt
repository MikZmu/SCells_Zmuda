package com.example.myapplication2

import com.example.scells_zmuda.adjustedCell
import com.example.solar_cells_v3.Matrix_Cell
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.min

class line() {

    var cells:ArrayList<adjustedCell> = ArrayList()
    var sum:ArrayList<Double> = ArrayList()
    var normalisedY:ArrayList<Double> = ArrayList()
    var maxX:Double = 0.0
    var maxY:Double = 0.0
    var gridX:ArrayList<Double> = ArrayList()
    var perfectGridX:ArrayList<Double> = ArrayList()
    var gridY:ArrayList<Double> = ArrayList()
    var nonZero:Boolean = false
    var perfectSum:ArrayList<Double> = ArrayList()
    var perfectNormalised:ArrayList<Double> = ArrayList()

    init {
        for(i in 0..74){
            cells.add(adjustedCell("zeros."))
            gridX.add(0.0)
            gridY.add(0.0)
            perfectGridX.add(0.0)
        }
        nonZero = false
    }

    fun modify(startIndex:Int,endIndex:Int, name:String, temp:Double,illu:Double){
        var i = startIndex;
        while (i <= endIndex){
            this.cells.get(i).changeParams(name,illu, temp)
            i++;
        }
        this.gridY = this.cells.get(shortestYindex()).yGridArray
        this.nonZero = nonZero()
        var sums = calculateSum()
        this.sum = sums[0]
        this.perfectSum = sums[1]
        this.maxX = this.maxX()
        this.maxY = this.maxY()
        this.gridX = Matrix_Cell.gridX(0.01, sum.max())
        this.perfectGridX = Matrix_Cell.gridX(0.01, perfectSum.max())
        var normSums = this.normaliseYFun()
        this.normalisedY = normSums[0]
        this.perfectNormalised = normSums[1]
        this.gridX.add(this.gridX.max()*1.005)
        this.perfectGridX.add(this.perfectGridX.max()*1.005)
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
            if(i.normalisedXArray.size <= returnVal && !(i.name != "zeros.")){
                returnVal = i.normalisedXArray.size
                returnIndex = this.cells.indexOf(i)
            }
        }
        return returnIndex

    }

    fun smallestY():Double{
        var min:Double = 999999.0
        for(i in this.cells){
            if(i.name != "zeros." && i.yGridArray.max()<min){
                min = i.yGridArray.max()
            }
        }
    return min
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










    fun calculateSum():ArrayList<ArrayList<Double>>{
        var rtArray:ArrayList<ArrayList<Double>> = ArrayList()
        rtArray.add(ArrayList())
        rtArray.add(ArrayList())
        if(nonZero){
            var longest = this.cells.get(longestYindex()).yGridArray.size
            var shortest = this.gridY.size
            var stopper = 0
            while (stopper <shortest){
                rtArray[0].add(0.0)
                stopper++
            }
            stopper = 0
            while (stopper <longest){
                rtArray[1].add(0.0)
                stopper++
            }
            stopper = 0

            while (stopper<longest){
                for(i in this.cells){
                    if(i.name != "zeros."){
                        if(stopper<shortest){
                            rtArray[0][stopper] += i.normalisedXArray.getOrElse(i.normalisedXArray.size-stopper){0.0}
                        }
                            rtArray[1][stopper] += i.normalisedXArray.getOrElse(i.normalisedXArray.size-stopper){0.0}
                    }
                }
                stopper++
            }
            rtArray[0].add(0.0)
            rtArray[0].sort()
            rtArray[1].add(0.0)
            rtArray[1].sort()
            return rtArray
        } else {
            rtArray[0].add(0.0)
            rtArray[1].add(0.0)
            return rtArray
        }
    }



    fun normaliseYFun():ArrayList<ArrayList<Double>>{

        var returnArray:ArrayList<ArrayList<Double>> = ArrayList()
        returnArray.add(ArrayList())
        returnArray.add(ArrayList())
        if(nonZero){
            returnArray[0] = Matrix_Cell.normaliseYFun(this.sum, this.gridX,this.gridY)
            returnArray[1] = Matrix_Cell.normaliseYFun(this.perfectSum, this.perfectGridX, this.cells[longestYindex()].yGridArray)
        } else {
            returnArray[0].add(0.0)
            returnArray[1].add(0.0)
        }
        var smallest:Double = this.smallestY()
        var i = 0
        while (i<returnArray[0].size){
            if(returnArray[0][i]>smallest){
                returnArray[0][i] = smallest
            }
            i++
        }
        returnArray[0].sortDescending()
        returnArray[1].sortDescending()
        return returnArray

    }

    fun nonZero():Boolean{
        var returnValue:Boolean = false

        for(cell in this.cells){
            if(cell.name != ("zeros.")){
                returnValue = true
            }
        }
        return returnValue
    }




}
