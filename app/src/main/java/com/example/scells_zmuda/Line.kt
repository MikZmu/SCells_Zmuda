package com.example.myapplication2
import com.example.scells_zmuda.AdjustedCell
import com.example.solar_cells_v3.MatrixCell

class Line() {

    var perfectNormalisedY: ArrayList<Double> = ArrayList()
    var cells:ArrayList<AdjustedCell> = ArrayList()
    var sum:ArrayList<Double> = ArrayList()
    var normalisedY:ArrayList<Double> = ArrayList()
    var gridX:ArrayList<Double> = ArrayList()
    var perfectGridX:ArrayList<Double> = ArrayList()
    var gridY:ArrayList<Double> = ArrayList()
    var nonZero:Boolean = false
    var perfectSum:ArrayList<Double> = ArrayList()
    var maxVal:Double = 0.0
    var maxGridY:ArrayList<Double> = ArrayList()
    var minVal:Double =0.0
    var minGridY:ArrayList<Double> = ArrayList()
    var minStep = 0.0
    var maxStep = 0.0

    init {
        for(i in 0..74){
            cells.add(AdjustedCell("zeros."))
            gridX.add(0.0)
            gridY.add(0.0)
            perfectGridX.add(0.0)
        }
        nonZero = false
    }

    fun modify(startIndex:Int,endIndex:Int, name:String, temp:Double,illu:Double){
        var i = startIndex
        val adc = AdjustedCell(name,temp,illu)
        if(adc.name != "zeros."){
            adc.changeParams(name,illu,temp)
        }
        while (i <= endIndex){
            this.cells[i] = adc
            i++;
        }
        this.nonZero = nonZero()

    }

    fun updatept1(){
        this.maxVal = this.gtMaxVal()
        this.minVal = this.gtMinVal()
        this.minStep = MatrixCell.stepCalc(this.minVal)
        this.maxStep = MatrixCell.stepCalc(this.minVal)
        this.maxGridY = MatrixCell.gridY(this.maxStep, this.maxVal)
        this.minGridY = MatrixCell.gridY(this.minStep, this.minVal)

        for(i in this.cells){
            i.updateNormX(minStep,minVal)
            i.updatePrfktX(maxStep, maxVal)
        }

        this.perfectSum = this.prfktSum()
        this.sum = this.normSum()
    }

    fun updatept2(minStep:Double, maxStep:Double){
        this.gridX = MatrixCell.gridX(minStep, sum[sum.size-1])
        this.perfectGridX = MatrixCell.gridX(maxStep, perfectSum[perfectSum.size-1])
        this.normalisedY = this.normaliseNormalYFun()
        this.perfectNormalisedY = this.normalisePrfktFun()
        val normZero = MatrixCell.calcZero(this.gridX, this.normalisedY)
        val perfZero = MatrixCell.calcZero(this.perfectGridX,this.perfectNormalisedY)
        this.gridX.add(normZero)
        this.perfectGridX.add(perfZero)
    }


    fun gtMaxVal():Double{
        var max:Double = 0.0
        if(nonZero){
            for(i in this.cells){
                if(i.yArray[0]>max){
                    max = i.yArray[0]
                }
            }
        }
        return max
    }

    fun gtMinVal():Double{
        var min:Double = 99999.0
        if(nonZero){
            for(i in this.cells){
                if(i.yArray[0]<min && i.name!="zeros."){
                    min = i.yArray[0]
                }
            }
        }
        return min
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
            if(i.normalisedXArray.size <= returnVal && (i.name != "zeros.")){
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


    fun prfktSum():ArrayList<Double>{
        val rtArray:ArrayList<Double> = ArrayList()
        if(nonZero){
            val longest = this.maxGridY.size
            var stopper = 0
            while (stopper <longest){
                rtArray.add(0.0)
                stopper++
            }
            stopper = 0
            while (stopper<longest){
                for(i in this.cells){
                    if(i.name != "zeros."){
                        rtArray[stopper] += i.perfectXArray.getOrElse(i.perfectXArray.size-stopper){0.0}
                    }
                }
                stopper++
            }
            rtArray.sort()
            return rtArray

        } else{
            rtArray.add(0.0)
            return rtArray
        }
    }



    fun normSum():ArrayList<Double>{
        val rtArray:ArrayList<Double> = ArrayList()
        if(nonZero){
            val shortest = this.minGridY.size
            var stopper = 0
            while (stopper <shortest){
                rtArray.add(0.0)
                stopper++
            }
            stopper = 0

            while (stopper<shortest){
                for(i in this.cells){
                    if(i.name != "zeros."){
                        rtArray[stopper] += i.normalisedXArray.getOrElse(i.normalisedXArray.size-stopper){0.0}
                    }
                }
                stopper++
            }
            rtArray.add(0.0)
            rtArray.sort()
            return rtArray
        } else {
            rtArray.add(0.0)
            return rtArray
        }
    }



fun normaliseNormalYFun():ArrayList<Double>{

    var returnArray:ArrayList<Double> = ArrayList()
    if(nonZero){
        returnArray = MatrixCell.normaliseYFun(this.sum, this.gridX,this.minGridY)
    } else {
        returnArray.add(0.0)
    }
    var smallest:Double = this.smallestY()
    var i = 0

    val smallestfun = this.cells.get(shortestYindex())
    val smallestnorm = MatrixCell.normaliseYFun(smallestfun.normalisedXArray, smallestfun.xGridArray,this.minGridY)
    while (i<returnArray.size){
        if(returnArray[i] > smallestfun.yArray[0]){
            returnArray[i] = smallestfun.yArray[0]
        }
        i++
    }
    returnArray.sortDescending()
    return returnArray

}

fun normalisePrfktFun():ArrayList<Double>{
    var returnArray:ArrayList<Double> = ArrayList()
    if(nonZero){
        returnArray = MatrixCell.normaliseYFun(this.perfectSum, this.perfectGridX,this.maxGridY)
    } else {
        returnArray.add(0.0)
    }
    returnArray.sortDescending()
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
