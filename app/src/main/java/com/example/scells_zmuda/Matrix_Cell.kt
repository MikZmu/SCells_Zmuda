package com.example.solar_cells_v3
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.abs


class Matrix_Cell(val name: String,
                  val xArray:ArrayList<Double>,
                  val yArray:ArrayList<Double>,
                  val startingTemp:Double,
                  val startingIllu:Double,
                  val v_temp_coe:Double,
                  val i_temp_coe:Double,
                  val i_illu_coe:Double,
                  val xGridArray:ArrayList<Double> = gridX(0.01,xArray.max()),
                  val yGridArray: ArrayList<Double> = gridY(0.01, yArray.max()),
                  val normalizedYArray:ArrayList<Double> = normaliseYFun(xArray,xGridArray, yArray),
                  val normalisedXArray:ArrayList<Double> = normaliseXFun(yArray,yGridArray,xArray),
)
 {


    fun returnName():String{
        return this.name
    }

    fun returnAsString():String{
        var i = 0
        var returnString:String =""
        while(i < xGridArray.size){
            returnString+=this.xGridArray.get(i).toString() + ":" + this.normalizedYArray.get(i)+"\n"
            i++
        }
        return returnString

    }

     fun returnnormalizedYArray():ArrayList<Double>{
         return  this.normalizedYArray
     }



    fun returnYAsDataPoints():LineGraphSeries<DataPoint?>{
        var i = 0
        var dataArray:Array<DataPoint?> = arrayOfNulls<DataPoint>(xGridArray.size.toInt())
        while (i<this.xGridArray.size){
            var dummyDP:DataPoint = DataPoint(this.xGridArray.get(i), this.normalizedYArray.get(i))
            dataArray[i] = dummyDP
            i++
        }
        val returnSeries:LineGraphSeries<DataPoint?> = LineGraphSeries(dataArray)

        return returnSeries
    }


     fun returnXAsDataPoints():LineGraphSeries<DataPoint?>{
         var i = 0
         var dataArray:Array<DataPoint?> = arrayOfNulls<DataPoint>(yGridArray.size.toInt())
         var arr2 = this.normalisedXArray.sorted()
         while (i<this.yGridArray.size){
             var dummyDP:DataPoint = DataPoint(arr2.get(i), this.yGridArray.get(i))
             dataArray[i] = dummyDP
             i++
         }

         val returnSeries:LineGraphSeries<DataPoint?> = LineGraphSeries(dataArray)
         return returnSeries
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

            var pair:Pair<ArrayList<Double>,ArrayList<Double>> = Pair(xArray, yArray)
            var returnX:ArrayList<Double> = ArrayList()
            var returnY:ArrayList<Double> = ArrayList()
            var i =0
            while (i<pair.first.size-1){

                var res1:Double = pair.first[i] + (temp - startingTemp) *v_temp_coe
                var res2:Double = pair.second[i] + (temp-startingTemp)*i_temp_coe + (illu-startingIllu)*i_illu_coe
                if(res1 >0 && res2 >0){
                    returnX.add(res1)
                    returnY.add(res2)
                }
                i++
            }
            var a1 = (returnY[returnY.size - 3] - returnY[returnY.size - 4])/(returnX[returnX.size - 3] - returnX[returnX.size - 4])
            var a2 = (returnY[returnY.size - 2] - returnY[returnY.size - 3])/(returnX[returnX.size - 2] - returnX[returnX.size - 3])
            var a3 = (returnY[returnY.size - 1] - returnY[returnY.size - 2])/(returnX[returnX.size - 1] - returnX[returnX.size - 2])
            var avg:Double = (a1+a2+a3)/3
            var b:Double = -avg*returnX[returnX.size-1]
            var x:Double = -b/avg
            returnX.add(x)
            returnY.add(0.0)


            var returnPair:Pair<ArrayList<Double>,ArrayList<Double>> = Pair(returnX, returnY)
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


        fun gridY(step: Double, upperBound: Double): ArrayList<Double> {
            var i: Double = 0.toDouble()
            var index: Int = 0;
            var yArrayNormalised: ArrayList<Double> = ArrayList()
            while (i < upperBound) {
                yArrayNormalised.add(index, i)
                i += step
                index +=1
            }
            yArrayNormalised.sortDescending()
            return yArrayNormalised

        }
        fun normaliseXFun(baseArray:ArrayList<Double>, gridBaseArray:ArrayList<Double>, processedArray:ArrayList<Double>):ArrayList<Double>{
            var value:Double
            var arrayNormalised:ArrayList<Double> = ArrayList()
            var hlpArray:ArrayList<Double> = ArrayList()
            baseArray.add(0.0)
            processedArray.add(processedArray.max())
            for(x in gridBaseArray){
                var lesser = lesserXVal(x, baseArray)
                var greater = greaterXval(x, baseArray)
                var lesserIndex = baseArray.indexOf(lesser)
                var greaterIndex = baseArray.indexOf(greater)
                var a = (processedArray.get(greaterIndex) - processedArray.getOrElse(lesserIndex){0}.toDouble()) / (greater - lesser)
                var b = processedArray.getOrElse(lesserIndex){0.0} - a*lesser
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






    }
}