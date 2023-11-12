package com.example.solar_cells_v3
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import androidx.core.util.rangeTo
import java.io.InputStream
import java.lang.Exception


class cellList (val context: Context, var cellList:ArrayList<Matrix_Cell> = getData(context)){


    fun listCells():ArrayList<String>{
        var returnList:ArrayList<String> = ArrayList()
        for(i in this.cellList){
            returnList.add(i.returnName())
        }
        return returnList
    }

    fun getCell(cellName:String):Matrix_Cell{
        for(i in cellList){
            if(i.returnName().equals(cellName)){
                return i
            }
        }
        throw Exception("kill me")

    }




    companion object {


        fun getData(context: Context):ArrayList<Matrix_Cell>{
            var returnList:ArrayList<Matrix_Cell> = ArrayList()
            var list = context.assets.list("");
            if (list != null) {
                for(i in list){
                    if(i.contains(".csv")){
                        var startingTemp:Double = 25.0
                        var startingIllu:Double = 1000.0
                        var v_temp_coe:Double = -0.0024768
                        var i_temp_coe:Double = 0.00952
                        var i_illu_coe:Double = 0.01
                        var returnXArray = ArrayList<Double>()
                        var returnYArray = ArrayList<Double>()
                        val inputStream:InputStream = context.assets.open(i)
                        returnXArray.clear()
                        returnYArray.clear()
                        inputStream.bufferedReader().forEachLine {
                            var str = (it.replace(",","."))
                            var rt = Pair(str.split(';')[0],str.split(';')[1])

                            if(rt.first.equals("v_temp_coe")){
                                v_temp_coe = rt.second.toDouble()
                            } else if(rt.first.equals("startingTemp")){
                                startingTemp = rt.second.toDouble()
                            } else if(rt.first.equals("i_temp_coe")){
                                i_temp_coe = rt.second.toDouble()
                            } else if(rt.first.equals("i_illu_coe")){
                                i_illu_coe = rt.second.toDouble()
                            } else if(rt.first.equals("startingIllu")){
                                startingIllu = rt.second.toDouble()
                            }else{
                                returnXArray.add(rt.first.toDouble())
                                returnYArray.add(rt.second.toDouble())
                            }

                        }


                        returnList.add(Matrix_Cell(i.removeSuffix("csv"),returnXArray, returnYArray,startingTemp,startingIllu,v_temp_coe,i_temp_coe,i_illu_coe))
                    }

                }
            }
            return returnList
    }


    }




}