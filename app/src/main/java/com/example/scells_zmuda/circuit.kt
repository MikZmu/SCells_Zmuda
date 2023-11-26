package com.example.myapplication2

import com.example.solar_cells_v3.Matrix_Cell
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.pow

class circuit() {

    var lines:ArrayList<line> = ArrayList()
    var sum: ArrayList<Double> = ArrayList()
    var dataPoints:LineGraphSeries<DataPoint> = LineGraphSeries()
    var power:ArrayList<Double> = ArrayList()
    var powerPoints:LineGraphSeries<DataPoint> = LineGraphSeries()
    var gridX:ArrayList<Double> = ArrayList()
    var Uoc:Double = 0.0
    var Isc:Double = 0.0
    var Pmax:Double = 0.0
    var Umax:Double = 0.0
    var Imax:Double = 0.0
    var FF:Double = 0.0
    var optRes:Double = 0.0
    var perfectGridX:ArrayList<Double> = ArrayList()
    var perfectSum:ArrayList<Double> = ArrayList()
    var perfectDP:LineGraphSeries<DataPoint> = LineGraphSeries()


    init {
        gridX.add(0.0)
        gridX.add(0.1)
        sum.add(0.0)
        sum.add(0.1)
    }

    fun longest():Int{
        var longest:Int = 0
        for(i in lines){
            if (i.gridX.size > longest && i.nonZero()){
                longest = i.gridX.size
            }
        }
        return longest
    }

    fun longestIndex():Int{
        var longest:Int = 0
        var ind = 0
        for(i in lines){
            if(i.perfectGridX.size > longest && i.nonZero()){
                longest = i.perfectGridX.size
                ind = this.lines.indexOf(i)
            }
        }
        return ind
    }



    fun zeroCirc():Boolean{
        var rt:Boolean = false
        for(i in lines){
            if(i.nonZero){
                rt = true
            }
        }
        return rt

    }

    fun smallestVoltage():Double{
        var minVol = 999999999.0
        if(zeroCirc()){
            for(i in lines){
                if(i.nonZero == true){

                    if (i.sum.max()<minVol){
                        minVol = i.sum.max()
                    }
                }
            }
            return minVol
        }
        return 0.0
    }


    fun modify(startLine:Int, endLine:Int, startCell:Int,endCell:Int,illu:Double,temp:Double,name:String){
        var i = startLine
        var modded:ArrayList<Int> = ArrayList()
        while(i <= endLine){
            this.lines.get((i)/3).modify( (i).mod(3) * 25 + startCell, (i).mod(3)*25+ endCell,name,temp,illu)
            modded.add(i/3)
            i++
        }
        for(i in modded){
            if(this.lines[i].nonZero){
                this.lines[i].update()
            }
        }
        gridX = Matrix_Cell.gridX(0.01,smallestVoltage())
        perfectGridX = this.lines[longestIndex()].perfectGridX
        gridX.add(gridX.max()*1.005)
        var sums = calculateSum()
        sum = sums[0]
        perfectSum = sums[1]
        gridX.add(gridX.max()*1.005)
        perfectGridX.add(perfectGridX.max()*1.005)
        sum.add(0.0)
        perfectSum.add(0.0)
        gridX.sort()
        perfectGridX.sort()
        sum.sortDescending()
        perfectSum.sortDescending()
        dataPoints = returnAsDataPoints(this.gridX, this.sum)
        perfectDP = returnAsDataPoints(this.perfectGridX, this.perfectSum)
        this.Uoc = this.gridX.max()
        this.Isc = this.sum.max()
        var pData:ArrayList<ArrayList<Double>> = gp()
        this.Pmax = pData[0][0]
        this.Umax = pData[0][1]
        this.Imax = pData[0][2]
        this.FF = (Imax*Umax)/(Uoc*Isc)
        this.power = pData[1]
        this.optRes = this.Umax/this.Imax
        powerPoints = powerPoints()
    }



    fun gp(): ArrayList<ArrayList<Double>> {
        var i = 0
        var returnArray:ArrayList<ArrayList<Double>> = ArrayList()
        returnArray.add(ArrayList(3))
        returnArray.add(ArrayList())
        returnArray[0].add(0.0) // power
        returnArray[0].add(0.0) // up
        returnArray[0].add(0.0) // ip
        while(i<this.gridX.size){
            var power = gridX.get(i) * sum.get(i)
            returnArray[1].add(power)
            if(power > returnArray[0][0]){
                returnArray[0][0] = gridX.get(i) * sum.get(i)
                returnArray[0][1] = gridX.get(i)
                returnArray[0][2] = sum.get(i)
            }
        i++
        }
        return  returnArray
    }






    fun calculateSum():ArrayList<ArrayList<Double>> {
        var stopper: Int = 0
        var sumArray: ArrayList<ArrayList<Double>> = ArrayList()
        sumArray.add(ArrayList())
        sumArray.add(ArrayList())
        var lines:ArrayList<ArrayList<Double>> = ArrayList()
        for(i in this.lines){
            if(i.nonZero){
                lines.add(squish(this.gridX, i.gridX, i.normalisedY))
            }
        }
        while (stopper < this.gridX.size) {
            var sum: Double = 0.0
            for (i in lines) {
                    sum += i.get(stopper)
            }
            sumArray[0].add(sum)
            stopper++

        }
        stopper = 0
        while (stopper < this.perfectGridX.size){
            var sum:Double = 0.0
            for(i in this.lines){
                if(i.nonZero()){
                    sum+=i.perfectNormalised.getOrElse(stopper){0.0}
                }
            }
            sumArray[1].add(sum)
            stopper++
        }

        return sumArray
    }

    fun squish(gridBaseArray:ArrayList<Double>, toSquish:ArrayList<Double>, vToSquish:ArrayList<Double>):ArrayList<Double>{
        var i:Int = gridBaseArray.size
        var gridMax = gridBaseArray.max()
        var squishMax = toSquish.max()
        var rtrnArray:ArrayList<Double> = ArrayList()
        for(i in gridBaseArray){
            rtrnArray.add(vToSquish.get(closest(i, gridBaseArray, toSquish)))
        }

        return rtrnArray

    }

    fun closest(value:Double, baseArray: ArrayList<Double>, array:ArrayList<Double>): Int {
        var ratio = value/baseArray.max()
        var diff2 = (baseArray.min() - array.max()).absoluteValue
        var index = array.size - 1
        for(i in array){
            var ratio2 = i/array.max()
            var diff = (ratio2 - ratio).absoluteValue
            if(diff<=diff2){
                diff2 = diff
                index = array.indexOf(i)
            }

        }
        return index
    }


    fun powerPoints():LineGraphSeries<DataPoint>{
        var xReturnArray:ArrayList<Double> = this.gridX
        var yReturnArray:ArrayList<Double> = this.power
        var i =0
        var dataArray:Array<DataPoint?> = arrayOfNulls<DataPoint>(xReturnArray.size)
        while(i < xReturnArray.size){
            var dummyDP:DataPoint = DataPoint(xReturnArray.get(i),yReturnArray.get(i))
            dataArray[i] = dummyDP
            i++
        }
        var returnSeries:LineGraphSeries<DataPoint> = LineGraphSeries(dataArray)
        return returnSeries
    }

    fun getPower4Resistance(rs:Double): ArrayList<Double> {
        var i = 0
        var res = 0.0
        var rtArray:ArrayList<Double> = ArrayList(3)
        rtArray.add(0.0) // power
        rtArray.add(0.0) // up
        rtArray.add(0.0)
        var diff = 99999.0
        var diff2= 0.0

        while (i < this.sum.size){
            res = this.gridX[i]/this.sum[i]
            diff2 = abs(res - rs)
            if(diff2 < diff){
                diff = diff2
                rtArray[0] = this.gridX[i]
                rtArray[1] = this.sum[i]
                rtArray[2] = this.sum[i] * this.gridX[i]

            }
            i++

        }
        return rtArray

    }

    fun returnAsDataPoints(xReturnArray:ArrayList<Double>, yReturnArray:ArrayList<Double>): LineGraphSeries<DataPoint> {
        var i =0
        var dataArray:Array<DataPoint?> = arrayOfNulls<DataPoint>(xReturnArray.size)
        while(i < xReturnArray.size){
            var dummyDP:DataPoint = DataPoint(xReturnArray.get(i),yReturnArray.get(i))
            dataArray[i] = dummyDP
            i++
        }

        var returnSeries:LineGraphSeries<DataPoint> = LineGraphSeries(dataArray)
        return returnSeries

    }

    companion object{

        fun round(digits:Int, number:Double):Double{

            return kotlin.math.round(number * 10.0.pow(digits))/(10.0.pow(digits))


        }


    }









}