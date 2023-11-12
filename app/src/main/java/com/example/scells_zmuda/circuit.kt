package com.example.myapplication2

import com.example.solar_cells_v3.Matrix_Cell
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.PointsGraphSeries
import java.lang.NullPointerException

class circuit() {

    var lines:ArrayList<line> = ArrayList()
    var sum: ArrayList<Double> = ArrayList()
    var dataPoints:LineGraphSeries<DataPoint?> = LineGraphSeries()
    var gridX:ArrayList<Double> = ArrayList()

    init {
        gridX.add(0.0)
        gridX.add(0.1)
        sum.add(0.0)
        sum.add(0.1)
    }

    fun longest():Int{
        var longest:Int = 0
        for(i in lines){
            if (i.gridX.size > longest){
                longest = i.gridX.size
            }
        }
        return longest
    }


    fun shortestVal():Double{
        var shortest:Double = 999.9
        for(i in lines){
            if (i.gridX.getOrElse(gridX.size){0.0} < shortest){
                shortest = i.gridX.getOrElse(gridX.size){0.0}
            }
        }
        return shortest
    }

    fun shortestIndex():Int{
        var shortest:Double = lines.get(0).gridX.getOrElse(gridX.lastIndex){999999999999999999.0}
        var shortestIndex:Int = 0
        for(i in lines){
            if (i.gridX.max() < shortest && i.nonZero.equals(true)){
                shortest = i.gridX.max()
                shortestIndex = gridX.indexOf(shortest)
            }
        }
        return shortestIndex

    }

    fun smallestVoltage():Double{
        var minVol = lines.get(0).sum.max()
        for(i in lines){
            if (i.sum.max()<minVol){
                minVol = i.sum.max()
            }
        }
        return minVol
    }


    fun modify(startLine:Int, endLine:Int, startCell:Int,endCell:Int,illu:Double,temp:Double,name:String){
        for(line in startLine until endLine){
            lines.get(line).modify(startCell,endCell,name,temp,illu)
        }
        sum = normaliseSum()
        gridX = lines.get(shortestIndex()).gridX
        dataPoints = returnAsDataPoints()
    }


    fun longestIndex():Int{
        var returnIndex:Int = 0
        var returnVal:Int = 0
        for(i in this.lines){
            var normalisedArr:ArrayList<Double> = Matrix_Cell.gridX(i.calculateSum(), 0.01, i.calculateSum().max())
            if(normalisedArr.size > returnVal){
                returnVal = normalisedArr.size
                returnIndex = this.lines.indexOf(i)
            }
        }
        return returnIndex
    }



    fun calculateSum():ArrayList<Double> {
        var stopper: Int = 0
        var sumArray: ArrayList<Double> = ArrayList()
        var longest = this.longest()
        while (stopper < longest) {
            var sum: Double = 0.0
            for (i in this.lines) {
                sum += i.normalisedY.getOrElse(stopper){0.0}
            }
            sumArray.add(sum)
            stopper++

        }

        return sumArray
    }

    fun normaliseSum():ArrayList<Double>{
        var shr:Double = shortestVal()
        var yArray = calculateSum()
        var hlpArray:ArrayList<Double> = ArrayList()
        hlpArray.add(0.0)
        var basedYArray:ArrayList<Double> = (hlpArray + yArray) as ArrayList<Double>
        var xArray = this.lines.get(longestIndex()).gridX
        for(i in xArray){
            if (i > shr){
                basedYArray.removeAt(xArray.indexOf(i))
            }
        }
        return basedYArray
    }

    fun returnAsDataPoints(): LineGraphSeries<DataPoint?> {
        var xReturnArray:ArrayList<Double> = this.lines.get(shortestIndex()).gridX
        var yReturnArray:ArrayList<Double> = this.sum
        var dataArray:Array<DataPoint?> = arrayOfNulls<DataPoint>(xReturnArray.size)
        for(i in yReturnArray){
            var dummyDP: DataPoint = DataPoint(xReturnArray.get(yReturnArray.indexOf(i)),i)
            dataArray[yReturnArray.indexOf(i)] =dummyDP
        }
        var returnSeries:LineGraphSeries<DataPoint?> = LineGraphSeries()
        try{
            returnSeries = LineGraphSeries(dataArray)
        } catch(e:NullPointerException) {

        }

        return returnSeries

    }







}