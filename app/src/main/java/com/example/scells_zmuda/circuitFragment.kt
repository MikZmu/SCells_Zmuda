package com.example.scells_zmuda

import android.graphics.Color
import android.graphics.ColorFilter
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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication2.helper
import com.example.scells_zmuda.databinding.FragmentCircuitBinding
import com.example.scells_zmuda.ui.theme.circuitFragmentViewModel
import com.example.solar_cells_v3.Matrix_Cell
import com.example.solar_cells_v3.cellList
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class circuitFragment : Fragment(),View.OnClickListener {
    private var _binding: FragmentCircuitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val vm:circuitFragmentViewModel by activityViewModels()
    private val binding get() = _binding!!
    var firstX:Int = 0;
    var firstY:Int = 0;
    var secondX:Int = 0;
    var secondY:Int = 0;
    var illu:Double = 0.0;
    var res:Double=0.0
    var cnter:Int=0;
    var name:String = "zeros"
    var temp:Double = 0.0

    



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
        val arrayAdapter = ArrayAdapter(requireContext(),
            androidx.appcompat.R.layout.select_dialog_item_material,helper.cellList.listCells())
        cells.adapter = arrayAdapter


        cells.setOnItemClickListener{adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
            name = cells.getItemAtPosition(i).toString()
        }
        cells.setItemChecked(0, true)
        name = cells.getItemAtPosition(0).toString()
        var uoc = binding.uocval
        var isc = binding.iscval
        var ff = binding.ffval
        var pmax = binding.pmaxval
        var upmax = binding.upmaxval
        var ipmax = binding.ipmaxval
        uoc.setText(helper.circuit.Uoc.toString() + " V")
        isc.setText(helper.circuit.Isc.toString()+ " A")
        ff.setText(helper.circuit.FF.toString())
        pmax.setText(helper.circuit.Pmax.toString() + " W")
        upmax.setText(helper.circuit.Umax.toString() + " V")
        ipmax.setText(helper.circuit.Imax.toString() + " A")

        button.setOnClickListener {
            if(vm.getCnter() == 2 || vm.getCnter() == 0){
                this.temp = tempFLD.text.toString().toDouble()
                this.illu = illuFLD.text.toString().toDouble()
                modCircuit(vm.getFX(),vm.getSX(),vm.getFY(),vm.getSY())
                vm.changeData(vm.getFX(),vm.getSX(),vm.getFY(),vm.getSY(),this.illu,this.temp,this.name)
                this.clrBtns()
                uoc.setText(helper.circuit.Uoc.toString() + " V")
                isc.setText(helper.circuit.Isc.toString()+ " A")
                ff.setText(helper.circuit.FF.toString())
                pmax.setText(helper.circuit.Pmax.toString() + " W")
                upmax.setText(helper.circuit.Umax.toString() + " V")
                ipmax.setText(helper.circuit.Imax.toString() + " A")
            }
        }


        var toggle = binding.toggle


        var eighty:ImageButton = binding.eightyPercent
        var sixty:ImageButton = binding.sixtyPercent
        var forty:ImageButton = binding.fourtyPercent
        var twenty:ImageButton = binding.twentyPercent
        var zero:ImageButton = binding.zeroPercent

        if(vm.getToggle()){
            eighty.setColorFilter(Color.argb(255,0,255,230))
            sixty.setColorFilter(Color.argb(255,0,255,180))
            forty.setColorFilter(Color.argb(255,0,255,130))
            twenty.setColorFilter(Color.argb(255,0,255,80))
            zero.setColorFilter(Color.argb(255,0,255,30))
        } else {
            eighty.setColorFilter(Color.argb(255,255,230,0))
            sixty.setColorFilter(Color.argb(255,255,180,0))
            forty.setColorFilter(Color.argb(255,255,130,0))
            twenty.setColorFilter(Color.argb(255,255,80,0))
            zero.setColorFilter(Color.argb(255,255,30,0))
        }


        toggle.setOnClickListener {
            vm.toggleToggle()
            if(vm.getToggle()){
                toggle.setText("TEMP")
            } else{
                toggle.setText("ILLU")
            }


            if(vm.getToggle()){
                eighty.setColorFilter(Color.argb(255,0,255,230))
                sixty.setColorFilter(Color.argb(255,0,255,180))
                forty.setColorFilter(Color.argb(255,0,255,130))
                twenty.setColorFilter(Color.argb(255,0,255,80))
                zero.setColorFilter(Color.argb(255,0,255,30))
            } else {
                eighty.setColorFilter(Color.argb(255,255,230,0))
                sixty.setColorFilter(Color.argb(255,255,180,0))
                forty.setColorFilter(Color.argb(255,255,130,0))
                twenty.setColorFilter(Color.argb(255,255,80,0))
                zero.setColorFilter(Color.argb(255,255,30,0))
            }
            clrBtns()
        }


        clrBtns()
        this.markButtons(vm.getFX(),vm.getSX(),vm.getFY(),vm.getSY(),vm.getCnter())




        return root
    }


    fun modCircuit(firstX:Int, secondX:Int, firstY:Int, secondY:Int){
        var a:Int=0
        var b:Int=0
        var c:Int=0
        var d:Int=0

        if(firstX >= secondX){
            a = secondX
            b = firstX
        }
        if(secondX > firstX){
            a = firstX
            b = secondX
        }

        if(firstY >= secondY){
            c  = secondY
            d = firstY
        }
        if(firstY<secondY){
            c = firstY
            d = secondY
        }
        helper.circuit.modify(a,b,c,d,illu,temp, name)

    }



    override fun onClick(v:View){
        var ind:Pair<Int,Int> = findBtn(v)
        if(vm.getCnter() == 0){
            markButtons(vm.getFX(), vm.getSX(), vm.getFY(), vm.getSY(), vm.getCnter())
            vm.incCnter()
        } else if(vm.getCnter() == 1){
            vm.setFX(ind.first)
            vm.setFY(ind.second)
            vm.setSX(ind.first)
            vm.setSY(ind.second)
            markButtons(vm.getFX(), vm.getSX(), vm.getFY(), vm.getSY(), vm.getCnter())
            vm.incCnter()
        } else if(vm.getCnter() == 2){
            vm.setSX(ind.first)
            vm.setSY(ind.second)
            markButtons(vm.getFX(), vm.getSX(), vm.getFY(), vm.getSY(), vm.getCnter())
            vm.resCnter()
        }





    }


    fun markButtons(firstX:Int, secondX:Int, firstY:Int, secondY:Int, counter:Int){
        var a:Int=0
        var b:Int=0
        var c:Int=0
        var d:Int=0

        if(firstX >= secondX){
            a = secondX
            b = firstX
        }
        if(secondX > firstX){
            a = firstX
            b = secondX
        }

        if(firstY >= secondY){
            c  = secondY
            d = firstY
        }
        if(firstY<secondY){
            c = firstY
            d = secondY
        }

        if( vm.getCnter() == 0 ){
            for(row in helper.buttons){
                for(btn in row){
                    btn.imageAlpha = 255
                }
            }
        }
        if(vm.getCnter() ==1 || vm.getCnter() == 2){
            for(index in a until b+1){
                var hpa = helper.buttons.get(index)
                for(xedni in c until d+1){
                    var btn = hpa.get(xedni)
                    btn.imageAlpha = 125
                }
            }
        }



    }

    fun clrBtns(){
        var matrix:ArrayList<ArrayList<Int>> = ArrayList()
        if(vm.getToggle()){
            matrix = vm.getTemp()
        } else {
            matrix = vm.getIllu()
        }
        for(row in helper.buttons){

            for(col in row){
                var color:Int = matrix.get(helper.buttons.indexOf(row)).get(row.indexOf(col))
                if(vm.getCells().get(helper.buttons.indexOf(row)).get(row.indexOf(col)) == "zeros."){
                    col.setColorFilter(Color.argb(255,128,128,128))
                }
                else if(vm.getToggle()){
                    col.setColorFilter(Color.argb(255,0,255,color))
                } else {
                    col.setColorFilter(Color.argb(255,255,color,0))
                }

            }

        }
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