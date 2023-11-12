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
                  val xGridArray:ArrayList<Double> = gridX(xArray,0.01,xArray.max()),
                  val yGridArray: ArrayList<Double> = gridY(yArray, 0.01, yArray.max()),
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

            for(i in pair.first){

                returnX.add(i + (temp - startingTemp) *v_temp_coe)

            }

            for (i in pair.second){
                returnY.add(i + (temp-startingTemp)*i_temp_coe + (illu-startingIllu)*i_illu_coe)
            }

            var returnPair:Pair<ArrayList<Double>,ArrayList<Double>> = Pair(returnX, returnY)
            return returnPair
        }





        fun gridX(
            xArray: ArrayList<Double>,
            step: Double,
            upperBound: Double): ArrayList<Double> {
            var i: Double = 0.toDouble()
            var index: Int = 0
            var xArrayNormalised: ArrayList<Double> = ArrayList()

            while (i < upperBound) {
                xArrayNormalised.add(index, i)
                i += step
                index +=1
            }
            xArrayNormalised.add(index, xArrayNormalised.get(xArrayNormalised.size-1)*1.001)
            return xArrayNormalised

        }


        fun gridY(yArray: ArrayList<Double>, step: Double, upperBound: Double): ArrayList<Double> {
            var i: Double = 0.toDouble()
            var index: Int = 0;
            var yArrayNormalised: ArrayList<Double> = ArrayList()
            while (i < upperBound) {
                yArrayNormalised.add(index, i)
                i += step
                index +=1
            }
            yArrayNormalised.add(0.0)
            yArrayNormalised.add(yArrayNormalised.max())
            yArrayNormalised.sort()
            return ArrayList((yArrayNormalised).reversed())

        }
        fun normaliseXFun(baseArray:ArrayList<Double>, gridBaseArray:ArrayList<Double>, processedArray:ArrayList<Double>):ArrayList<Double>{
            var value:Double
            var arrayNormalised:ArrayList<Double> = ArrayList()
            var hlpArray:ArrayList<Double> = ArrayList()
            hlpArray.add(0.0)
            var basedArray:ArrayList<Double> = (hlpArray + baseArray) as ArrayList<Double>
            for(x in gridBaseArray){
                var lesser = lesserVal(x, basedArray)
                var greater = greaterVal(x, basedArray)
                var lesserIndex = basedArray.indexOf(lesser)
                var greaterIndex = basedArray.indexOf(greater)
                var a = (processedArray.get(greaterIndex) - processedArray.getOrElse(lesserIndex){0}.toDouble()) / (greater - lesser)
                var b = processedArray.getOrElse(lesserIndex){0.0} - a*lesser
                value = a*lesser+b
                if(value.isNaN()){
                    value = processedArray.max()
                }
                arrayNormalised.add(value)

            }
            arrayNormalised.set(0, 0.0)
            arrayNormalised.sortedDescending().reversed()
            return arrayNormalised
        }

        fun normaliseYFun(baseArray:ArrayList<Double>, gridBaseArray:ArrayList<Double>, processedArray:ArrayList<Double>):ArrayList<Double>{
            var value:Double
            var arrayNormalised:ArrayList<Double> = ArrayList()
            var hlpArray:ArrayList<Double> = ArrayList()
            hlpArray.add(0.0)
            var basedArray:ArrayList<Double> = (hlpArray + baseArray) as ArrayList<Double>
            for(x in gridBaseArray){
                var lesser = lesserVal(x, basedArray)
                var greater = greaterVal(x, basedArray)
                var lesserIndex = basedArray.indexOf(lesser)
                var greaterIndex = basedArray.indexOf(greater)
                var a = (processedArray.get(greaterIndex) - processedArray.getOrElse(lesserIndex){0}.toDouble()) / (greater - lesser)
                var b = processedArray.getOrElse(lesserIndex){0.0} - a*lesser
                value = a*lesser+b
                if(value.isNaN()){
                    value = processedArray.max()
                }
                arrayNormalised.add(value)
            }
            arrayNormalised.set(arrayNormalised.size-1, 0.0)
            return arrayNormalised
        }

        fun getMp(vArray:ArrayList<Double>, iArray: ArrayList<Double>):Double{
            var stopper = 0
            var power1 = 0.0
            var power2:Double
            while(stopper<vArray.size){
                power2 = vArray.get(stopper) * iArray.get(stopper)
                if(power2>power1){
                    power1 = power2
                }
                stopper++
            }
            return power1
        }
        fun getIMp(vArray:ArrayList<Double>, iArray: ArrayList<Double>):Double{
            var stopper = 0
            var power1 = 0.0
            var power2:Double
            var IMp:Double = 0.0
            while(stopper<vArray.size){
                power2 = vArray.get(stopper) * iArray.get(stopper)
                if(power2>power1){
                    power1 = power2
                    IMp = iArray.get(stopper)
                }
                stopper++
            }
            return IMp
        }
        fun getUMp(vArray:ArrayList<Double>, iArray: ArrayList<Double>):Double{
            var stopper = 0
            var power1 = 0.0
            var power2:Double
            var UMp:Double = 0.0
            while(stopper<vArray.size){
                power2 = vArray.get(stopper) * iArray.get(stopper)
                if(power2>power1){
                    power1 = power2
                    UMp = vArray.get(stopper)
                }
                stopper++
            }
            return UMp
        }
        fun getUoc(xArray: ArrayList<Double>):Double{

            return xArray.sorted().get(xArray.size-1)
        }

        fun getIsc(yArray: ArrayList<Double>):Double{
            return yArray.sorted().reversed().get(0)
        }





        fun lesserVal(value: Double, dataSet: ArrayList<Double>): Double {
            var rtList:ArrayList<Double> = ArrayList()
            for(x in dataSet){
                if(x<=value){
                    rtList.add(x)
                }
            }
            rtList.add(dataSet.min())
            return rtList.sorted().reversed()[0]

        }

        fun greaterVal(value: Double, dataSet: ArrayList<Double>): Double {
            var rtList:ArrayList<Double> = ArrayList()
            rtList.add(0.0)
            for (x in dataSet) {
                if (x >= value) {
                    rtList.add(x)
                }
            }
            return rtList.sorted()[0]
        }






    }
}