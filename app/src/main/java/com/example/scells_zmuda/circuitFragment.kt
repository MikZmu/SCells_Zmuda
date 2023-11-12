package com.example.scells_zmuda

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import com.example.myapplication2.helper
import com.example.scells_zmuda.databinding.FragmentCircuitBinding
import com.example.solar_cells_v3.Matrix_Cell
import com.example.solar_cells_v3.cellList
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class circuitFragment : Fragment(),View.OnClickListener {
    private var _binding: FragmentCircuitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var firstX:Int = 0;
    var firstY:Int = 0;
    var secondX:Int = 0;
    var secondY:Int = 0;
    var illu:Double = 0.0;
    var uoc:Double = 0.0;
    var isc:Double=0.0;
    var umax:Double=0.0;
    var pmax:Double=0.0
    var imax:Double=0.0
    var res:Double=0.0
    var cnter:Int=0;
    var name:String = "zeros"
    var temp:Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if(savedInstanceState == (null)){

        } else {
            var firstX:Int = savedInstanceState.getInt("firstX")
            var firstY:Int = savedInstanceState.getInt("firstY")
            var secondX:Int = savedInstanceState.getInt("secondX")
            var secondY:Int = savedInstanceState.getInt("secondY")
            var uoc:Double = savedInstanceState.getDouble("uoc")
            var isc:Double=savedInstanceState.getDouble("isc")
            var umax:Double=savedInstanceState.getDouble("umax")
            var pmax:Double=savedInstanceState.getDouble("pmax")
            var imax:Double=savedInstanceState.getDouble("imax")
            var res:Double=savedInstanceState.getDouble("res")
            var cnter:Int=savedInstanceState.getInt("cnter")

            markButtons(firstX, firstY, secondX, secondY, cnter)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        fun getBtns():ArrayList<ArrayList<ImageButton>>{
            var buttons:ArrayList<ArrayList<ImageButton>> = ArrayList()
            val c0:View = binding.c0.linearLayout
            val c1:View = binding.c1.linearLayout
            val c2:View = binding.c2.linearLayout
            val c3:View = binding.c3.linearLayout
            val c4:View = binding.c4.linearLayout
            val c5:View = binding.c5.linearLayout
            val c6:View = binding.c6.linearLayout
            val c7:View = binding.c7.linearLayout
            val c8:View = binding.c8.linearLayout



            fun toRow(view: View):ArrayList<ImageButton>{
                var btnList:ArrayList<ImageButton> = ArrayList()
                for(xedni in 0 until 25){
                    var rowtag = "r" + xedni.toString()
                    var btn = view.findViewWithTag<ImageButton>(rowtag)
                    btn.setOnClickListener(this)
                    btnList.add(btn)
                }
                return btnList
            }
            buttons.add(toRow(c0))
            buttons.add(toRow(c1))
            buttons.add(toRow(c2))
            buttons.add(toRow(c3))
            buttons.add(toRow(c4))
            buttons.add(toRow(c5))
            buttons.add(toRow(c6))
            buttons.add(toRow(c7))
            buttons.add(toRow(c8))


            return buttons
        }

















        _binding = FragmentCircuitBinding.inflate(inflater, container, false)
        val root: View = binding.root
        helper.buttons = getBtns()
        markButtons(firstX,firstY,secondX,secondY,cnter)
        var button:Button = binding.button
        var illuFLD = binding.illumination
        var tempFLD = binding.temperature
        var cells:ListView = binding.cells
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.custom_listview,helper.cellList.listCells())
        cells.adapter = arrayAdapter

        cells.setOnItemClickListener{adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
            name = cells.getItemAtPosition(i).toString()
        }

        button.setOnClickListener {
            this.temp = tempFLD.text.toString().toDouble()
            this.illu = illuFLD.text.toString().toDouble()
            modCircuit(firstX,secondX,firstY,secondY,cnter)
        }



        return root
    }


    fun modCircuit(firstX:Int, secondX:Int, firstY:Int, secondY:Int, counter:Int){
        var a:Int=0
        var b:Int=0
        var c:Int=0
        var d:Int=0

        if(firstX > secondX){
            a = secondX
            b = firstX
        }
        if(secondX > firstX){
            a = firstX
            b = secondX
        }

        if(firstY > secondY){
            c  = secondY
            d = firstY
        }
        if(firstY<secondY){
            c = firstY
            d = secondY
        }
        if(counter == 0||counter == 1){
            helper.circuit.modify((a+1)/3,(b+1)/3,c,d,illu,temp, name)
        } else {

        }

    }



    override fun onClick(v:View){
        var ind:Pair<Int,Int> = findBtn(v)
        if(cnter == 0){
            markButtons(firstX, secondX, firstY, secondY, cnter)
            cnter++
        } else if(cnter == 1){
            firstX = ind.first
            firstY = ind.second
            markButtons(firstX, secondX, firstY, secondY, cnter)
            cnter++
        } else if(cnter == 2){
            secondX = ind.first
            secondY = ind.second
            markButtons(firstX, secondX, firstY, secondY, cnter)
            cnter=0
        }





    }

    fun markButtons(firstX:Int, secondX:Int, firstY:Int, secondY:Int, counter:Int){
        var a:Int=0
        var b:Int=0
        var c:Int=0
        var d:Int=0

        if(firstX > secondX){
            a = secondX
            b = firstX
        }
        if(secondX > firstX){
            a = firstX
            b = secondX
        }

        if(firstY > secondY){
            c  = secondY
            d = firstY
        }
        if(firstY<secondY){
            c = firstY
            d = secondY
        }

        if( counter == 0 ){
            for(row in helper.buttons){
                for(btn in row){
                    btn.imageAlpha = 255
                }
            }
        }
        if(counter == 1){
            var hpa = helper.buttons.get(firstX)
            var btn = hpa.get(firstY)
            btn.imageAlpha = 125
        }
        if(counter ==2){
            for(index in a until b+1){
                var hpa = helper.buttons.get(index)
                for(xedni in c until d+1){
                    var btn = hpa.get(xedni)
                    btn.imageAlpha = 125
                }
            }
        }



    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("firstX", firstX)
        outState.putInt("firstY", firstY)
        outState.putInt("secondX", secondX)
        outState.putInt("firstX", secondY)
        outState.putDouble("uoc", uoc)
        outState.putDouble("isc", isc)
        outState.putDouble("umax", umax)
        outState.putDouble("imax", imax)
        outState.putDouble("pmax", pmax)
        outState.putDouble("res", res)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }



    companion object{
        fun findBtn(btn:View):Pair<Int,Int>{
            for(row in helper.buttons){
                for(bt in row){
                    if (btn.equals(bt)){
                        var index = row.indexOf(bt)
                        var index2 = helper.buttons.indexOf(row)
                        return (Pair(index2,index))
                    }
                }

            }
            return Pair(0,0)
        }
    }




}