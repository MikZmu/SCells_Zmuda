package com.example.myapplication2

import com.example.solar_cells_v3.MatrixCell
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.pow

class Circuit() {

    var UocMax: Double = 0.0 // Napięcie rozwarcia układu potencjalnego
    var lines:ArrayList<Line> = ArrayList() // Linie połączeń szeregowych
    var sum: ArrayList<Double> = ArrayList() // Suma Połączenia Równoległego
    var minPowerPoints:LineGraphSeries<DataPoint> = LineGraphSeries() // Punkty wykresu P = f(U) dla połączenia rzeczywistego
    var maxPowerPoints:LineGraphSeries<DataPoint> = LineGraphSeries() // Punkty wykresu P = f(U) dla połączenia potencjalnego
    var mingridX:ArrayList<Double> = ArrayList() // Znormalizowany zestaw danych dla napięcia
    var UocMin:Double = 0.0 // Napięcie rozwarcia układu rzeczywistego
    var minGridX:ArrayList<Double> = ArrayList() // Znormalizowany zestaw danych dla napięcia
    var maxGridX:ArrayList<Double> = ArrayList() // Znormalizowany zestaw danych dla napięcia potencjalnego
    var minSum:ArrayList<Double> = ArrayList() // Suma Połączenia Równoległego rzeczywistego
    var maxSum:ArrayList<Double> = ArrayList() // Suma Połączenia Równoległego potencjalnego
    var mindataPoints:LineGraphSeries<DataPoint> = LineGraphSeries() // Punkty wykresu I = f(U) dla połączenia rzeczywistego
    var maxDataPoints:LineGraphSeries<DataPoint> = LineGraphSeries() // Punkty wykresu I = f(U) dla połączenia potencjalnego
    var minPower:ArrayList<Double> = ArrayList() // wynik obliczeń mocy dla połączenia rzeczywistego
    var maxPower:ArrayList<Double> = ArrayList() // wynik obliczeń mocy dla połączenia potencjanlego
    var PmaxMin:Double = 0.0  //Punkt mocy maksymalnej dla połączenia rzeczywistego
    var UmaxMin:Double = 0.0 //Napięcie rozwarcia dla połączenia potencjalnego
    var ImaxMin:Double = 0.0 //Prąd dla punktu mocy maksymalnej dla połączenia rzeczywistego
    var FFMin:Double = 0.0 //Prąd dla punktu mocy maksymalnej dla połączenia potencjalnego
    var charResMin:Double = 0.0 //Rezystancja charakterystyczna połączenia potencjalnego
    var IscMin:Double = 0.0 //Prąd Zwarcia połączenia rzeczywistego
    var IscMax:Double = 0.0 //Prąd Zwarcia połączenia potencjalnego
    var PmaxMax:Double = 0.0 //Moc Maksymalna połączenia potencjalnego
    var UmaxMax:Double = 0.0 //Napięcie mocy maksymalnej połączenia potencjalnego
    var ImaxMax:Double = 0.0 //Prąd mocy maksymalnej połączenia potencjalnego
    var FFMax:Double = 0.0 //Współczynnik wypełnienia połączenia potencjalnego



    init {
        var line1: Line = Line()
        var line2: Line = Line()
        var line3: Line = Line()
        this.lines.add(line1)
        this.lines.add(line2)
        this.lines.add(line3)
        mingridX.add(0.0)
        mingridX.add(0.1)
        sum.add(0.0)
        sum.add(0.1)
    }

    fun zeroCirc():Boolean{
        var rt = false
        for(i in lines){
            if(i.nonZero){
                rt = true
            }
        }
        return rt
    }


    fun modify(startLine:Int, endLine:Int, startCell:Int,endCell:Int,illu:Double,temp:Double,name:String){
        var i = startLine
        val modded:ArrayList<Int> = ArrayList()
        while(i <= endLine){
            this.lines[i/3].modify( (i).mod(3) * 25 + startCell, (i).mod(3)*25+ endCell,name,temp,illu)
            modded.add(i/3)
            i++
        }
        for(j in modded){
            if(this.lines[j].nonZero){
                this.lines[j].updatept1()
            }
        }
        val minX:Double = this.getminX()
        val maxX = this.getMaxX()
        for(j in modded){
            if(this.lines[j].nonZero){
                this.lines[j].updatept2(MatrixCell.stepCalc(minX), MatrixCell.stepCalc(maxX))
            }
        }
        this.minGridX = MatrixCell.gridX(MatrixCell.stepCalc(minX), minX)
        this.maxGridX= MatrixCell.gridX(MatrixCell.stepCalc(maxX), maxX)
        this.minSum = calcNormSum()
        this.maxSum = calculatePrfktSum()
        if(zeroCirc()){
            this.minGridX.add(MatrixCell.calcZero(this.minGridX, minSum))
            this.maxGridX.add(MatrixCell.calcZero(this.maxGridX, maxSum))
            this.minSum.add(0.0)
            this.maxSum.add(0.0)

        }
        this.minGridX.sort()
        this.maxGridX.sort()
        this.minSum.sortDescending()
        this.maxSum.sortDescending()
        this.mindataPoints = returnAsDataPoints(this.minGridX, this.minSum)
        this.maxDataPoints = returnAsDataPoints(this.maxGridX, this.maxSum)
        val minPdata:ArrayList<ArrayList<Double>> = gp(minGridX, minSum)
        val maxPdata:ArrayList<ArrayList<Double>> = gp(maxGridX, maxSum)
        this.UocMin = this.minGridX[minGridX.size-1]
        this.UocMax = this.maxGridX[maxGridX.size-1]
        this.IscMin = this.minSum[0]
        this.IscMax = this.maxSum[0]
        this.PmaxMin = minPdata[0][0]
        this.PmaxMax = maxPdata[0][0]
        this.UmaxMin = minPdata[0][1]
        this.UmaxMax = maxPdata[0][1]
        this.ImaxMin = minPdata[0][2]
        this.ImaxMax = maxPdata[0][2]
        this.FFMin = (ImaxMin*UmaxMin)/(UocMin*IscMin)
        this.FFMax = (ImaxMax*UmaxMax)/(UocMax*IscMax)
        this.minPower = minPdata[1]
        this.maxPower = maxPdata[1]
        this.charResMin = this.UmaxMin/this.ImaxMin
        this.minPowerPoints = returnAsDataPoints(this.minGridX, this.minPower)
        this.maxPowerPoints = returnAsDataPoints(this.maxGridX, this.maxPower)

    }


    fun getMaxX():Double{
        var maxX = 0.0
        for(i in this.lines){
            if(i.nonZero)
                if(i.perfectSum[i.perfectSum.size-1] > maxX) {
                    maxX = i.perfectSum[i.perfectSum.size - 1]
                }
            }
        return maxX
    }

    fun getminX():Double{
        var minX = 99999.0
        if(zeroCirc()==false){
            return 0.0
        }
        for(i in this.lines){
            if(i.nonZero){
                if(i.sum[i.sum.size-1] < minX){
                    minX = i.sum[i.sum.size-1]
                }
            }
        }
        return minX
    }



    fun gp(xArray:ArrayList<Double>, yArray:ArrayList<Double>): ArrayList<ArrayList<Double>> {
        var i = 0
        val returnArray:ArrayList<ArrayList<Double>> = ArrayList()
        returnArray.add(ArrayList(3))
        returnArray.add(ArrayList())
        returnArray[0].add(0.0) // power
        returnArray[0].add(0.0) // up
        returnArray[0].add(0.0) // ip
        while(i<xArray.size){
            val power = xArray.get(i) * yArray.get(i)
            returnArray[1].add(power)
            if(power > returnArray[0][0]){
                returnArray[0][0] = xArray.get(i) * yArray.get(i)
                returnArray[0][1] = xArray.get(i)
                returnArray[0][2] = yArray.get(i)
            }
        i++
        }
        return  returnArray
    }


    fun calcNormSum():ArrayList<Double>{
        var stopper: Int = 0
        val sumArray: ArrayList<Double> = ArrayList()
        val lines:ArrayList<ArrayList<Double>> = ArrayList()
        for(i in this.lines){
            if(i.nonZero){
                lines.add(squish(this.minGridX, i.gridX, i.normaliseNormalYFun()))
            }
        }

        while (stopper < this.minGridX.size) {
            var sum: Double = 0.0
            for (i in lines) {
                sum += i.get(stopper)
            }
            sumArray.add(sum)
            stopper++

        }
        return sumArray
    }

    fun calculatePrfktSum():ArrayList<Double>{
        var stopper: Int = 0
        val sumArray: ArrayList<Double> = ArrayList()
        val lines:ArrayList<ArrayList<Double>> = ArrayList()
        for(i in this.lines){
            if(i.nonZero){
                lines.add(squish(this.maxGridX, i.perfectGridX, i.normalisePrfktFun()))
            }
        }


        while (stopper < this.maxGridX.size) {
            var sum: Double = 0.0
            for (i in lines) {
                sum += i.get(stopper)
            }
            sumArray.add(sum)
            stopper++

        }
        return sumArray
    }



    fun squish(gridBaseArray:ArrayList<Double>, toSquish:ArrayList<Double>, vToSquish:ArrayList<Double>):ArrayList<Double>{
        val rtrnArray:ArrayList<Double> = ArrayList()
        for(l in gridBaseArray){
            rtrnArray.add(vToSquish.get(closest(l, gridBaseArray, toSquish)))
        }

        return rtrnArray
    }

    fun closest(value:Double, baseArray: ArrayList<Double>, array:ArrayList<Double>): Int {
        val ratio = value/baseArray.max()
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


    fun getPower4Resistance(rs:Double): ArrayList<Double> {
        var i = 0
        var res = 0.0
        val rtArray:ArrayList<Double> = ArrayList(3)
        rtArray.add(0.0) // power
        rtArray.add(0.0) // up
        rtArray.add(0.0)
        var diff = 99999.0
        var diff2= 0.0

        while (i < this.minSum.size){
            res = this.minGridX[i]/this.minSum[i]
            diff2 = abs(res - rs)
            if(diff2 < diff){
                diff = diff2
                rtArray[0] = this.minGridX[i]
                rtArray[1] = this.minSum[i]
                rtArray[2] = this.minSum[i] * this.minGridX[i]

            }
            i++

        }
        return rtArray

    }

    fun returnAsDataPoints(xReturnArray:ArrayList<Double>, yReturnArray:ArrayList<Double>): LineGraphSeries<DataPoint> {
        var i =0
        val dataArray:Array<DataPoint?> = arrayOfNulls<DataPoint>(xReturnArray.size)
        while(i < xReturnArray.size){
            val dummyDP:DataPoint = DataPoint(xReturnArray.get(i),yReturnArray.get(i))
            dataArray[i] = dummyDP
            i++
        }

        val returnSeries:LineGraphSeries<DataPoint> = LineGraphSeries(dataArray)
        return returnSeries

    }

    companion object{

        fun round(digits:Int, number:Double):Double{

            return kotlin.math.round(number * 10.0.pow(digits))/(10.0.pow(digits))


        }


    }









}