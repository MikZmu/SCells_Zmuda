package com.example.solar_cells_v3


class MatrixCell(val name: String,
                 val vArray:ArrayList<Double>,
                 val iArray:ArrayList<Double>,
                 val startingTemp:Double,
                 val startingIllu:Double,
                 val vTempCoe:Double,
                 val iTempCoe:Double,
                 val iIlluCoe:Double,
)
 {


    fun returnName():String{
        return this.name
    }




    companion object {

        fun adjustForParams(xArray:ArrayList<Double>
                            ,yArray:ArrayList<Double>
                            ,startingTemp: Double
                            ,v_temp_coe: Double
                            ,i_temp_coe: Double
                            ,temp:Double,startingIllu: Double
                            , i_illu_coe: Double
                            , illu:Double):Pair<ArrayList<Double>
                            ,ArrayList<Double>>{

            val pair:Pair<ArrayList<Double>,ArrayList<Double>> = Pair(xArray, yArray)
            val returnX:ArrayList<Double> = ArrayList()
            val returnY:ArrayList<Double> = ArrayList()
            var i =0
            while (i<pair.first.size-1){

                val res1:Double = pair.first[i] + (temp - startingTemp) *v_temp_coe
                val res2:Double = pair.second[i] + (temp-startingTemp)*i_temp_coe + (illu-startingIllu)*i_illu_coe
                if(res1 >0 && res2 >0){
                    returnX.add(res1)
                    returnY.add(res2)
                }
                i++
            }
            if(returnX.size >=3){
                val a1 = (returnY[returnY.size - 3] - returnY[returnY.size - 4])/(returnX[returnX.size - 3] - returnX[returnX.size - 4])
                val a2 = (returnY[returnY.size - 2] - returnY[returnY.size - 3])/(returnX[returnX.size - 2] - returnX[returnX.size - 3])
                val a3 = (returnY[returnY.size - 1] - returnY[returnY.size - 2])/(returnX[returnX.size - 1] - returnX[returnX.size - 2])
                val avg:Double = (a1+a2+a3)/3
                val b:Double = -avg*returnX[returnX.size-1]
                val x:Double = -b/avg
                returnX.add(x)
            } else {
                returnX.add(returnX.max())
            }
            returnY.add(0.0)
            val returnPair:Pair<ArrayList<Double>,ArrayList<Double>> = Pair(returnX, returnY)
            return returnPair
        }





        fun gridX(
            step: Double,
            upperBound: Double): ArrayList<Double> {
            var i: Double = 0.toDouble()
            var index: Int = 0
            var xArrayNormalised: ArrayList<Double> = ArrayList()
            if(step>=upperBound){
                xArrayNormalised.add(0.0)
                return xArrayNormalised
            }
            while (i <= upperBound) {
                xArrayNormalised.add(i)
                i += step
                index +=1
            }
            if(xArrayNormalised.size!=0){
                xArrayNormalised.add(index, xArrayNormalised.max()+step)
            }
            return xArrayNormalised

        }

        fun calcZero(xArray:ArrayList<Double>, yArray:ArrayList<Double>):Double{
            var a = (yArray[yArray.size-1] - yArray[yArray.size-2])/(xArray[xArray.size-1] - xArray[xArray.size-2])
            var b = (yArray[yArray.size-1]) - (a * xArray[xArray.size-1])
            var x = -b/a
            return x

        }


        fun gridY(step: Double, upperBound: Double): ArrayList<Double> {
            var i: Double = 0.toDouble()
            var index: Int = 0;
            val yArrayNormalised: ArrayList<Double> = ArrayList()
            while (i <= upperBound) {
                yArrayNormalised.add(index, i)
                i += step
                index +=1
            }
            yArrayNormalised.sortDescending()
            return yArrayNormalised

        }
        fun normaliseXFun(baseArray:ArrayList<Double>, gridBaseArray:ArrayList<Double>, processedArray:ArrayList<Double>):ArrayList<Double>{
            var value:Double
            val arrayNormalised:ArrayList<Double> = ArrayList()
            processedArray.add(processedArray.max())
            for(x in gridBaseArray){
                val lesser = lesserXVal(x, baseArray)
                val greater = greaterXval(x, baseArray)
                val lesserIndex = baseArray.indexOf(lesser)
                val greaterIndex = baseArray.indexOf(greater)
                val a = (processedArray.get(greaterIndex) - processedArray.getOrElse(lesserIndex){0}.toDouble()) / (greater - lesser)
                val b = processedArray.getOrElse(lesserIndex){0.0} - a*lesser
                value = a*lesser+b
                if(value.isNaN()){
                    value = processedArray.max()
                }
                arrayNormalised.add(value)

            }
            arrayNormalised.set(0, 0.0)
            arrayNormalised.sorted()
            return arrayNormalised
        }



        fun normaliseYFun(baseArray:ArrayList<Double>, gridBaseArray:ArrayList<Double>, processedArray:ArrayList<Double>):ArrayList<Double>{
            var value:Double
            var arrayNormalised:ArrayList<Double> = ArrayList()
            var hlpArray:ArrayList<Double> = ArrayList()
            hlpArray.add(0.0)
            var basedArray:ArrayList<Double> = (hlpArray + baseArray) as ArrayList<Double>
            for(x in gridBaseArray){
                var lesser = lesserXVal(x, basedArray)
                var greater = greaterXval(x, basedArray)
                var lesserIndex = basedArray.indexOf(lesser)
                var greaterIndex = basedArray.indexOf(greater)
                var a = (processedArray.getOrElse(greaterIndex){processedArray.max()} - processedArray.getOrElse(lesserIndex){0}.toDouble()) / (greater - lesser)
                var b = processedArray.getOrElse(lesserIndex){0.0} - a*lesser
                value = a*((greater+lesser)/2)+b
                if(value.isNaN()){
                    value = processedArray.max()
                }
                if(value == 0.0){
                    null
                }
                arrayNormalised.add(value)
            }
            arrayNormalised.add(0.0)
            arrayNormalised.sortDescending()
            return arrayNormalised
        }






        fun lesserXVal(value: Double, dataSet: ArrayList<Double>): Double {
            var rtList:ArrayList<Double> = ArrayList()
            for(x in dataSet){
                if(x<=value){
                    rtList.add(x)
                }
            }
            if(rtList.isEmpty()){
                return 0.0
            } else {
                return rtList.sorted().reversed()[0]
            }

        }

        fun greaterXval(value: Double, dataSet: ArrayList<Double>): Double {
            var rtList:ArrayList<Double> = ArrayList()
            for (x in dataSet) {
                if (x > value) {
                    rtList.add(x)
                }
            }
            if(rtList.isEmpty()){
                return dataSet.max()
            } else{
                return return rtList.sorted()[0]
            }

        }


        fun greaterVal(value: Double, dataSet: ArrayList<Double>): Double {
            var rtList:ArrayList<Double> = ArrayList()
            for (x in dataSet) {
                if (x > value) {
                    rtList.add(x)
                }
            }
            if(rtList.isEmpty()){
                return 0.0
            } else {
                return rtList.sorted()[0]
            }

        }

        fun stepCalc(value:Double):Double{
            if(value > 40){
                return 0.2
            } else if(value > 20){
                return 0.14
            } else if(value>10){
                return 0.08
            } else if(value>5){
                return 0.04
            } else if(value >2){
                return 0.02
            } else {
                return 0.01
            }


        }






    }
}