package com.example.scells_zmuda

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.myapplication2.Singleton
import com.example.scells_zmuda.databinding.FragmentCircuitBinding


class CircuitFragment : Fragment(),View.OnClickListener {
    private var _binding: FragmentCircuitBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val vm: circuitFragmentViewModel by activityViewModels()
    private val binding get() = _binding!!
    var illu:Double = 0.0;
    var res:Double=0.0
    var name:String = "zeros"
    var temp:Double = 0.0

    



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        fun getBtns():ArrayList<ArrayList<ImageButton>>{
            val buttons:ArrayList<ArrayList<ImageButton>> = ArrayList()
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
                val btnList:ArrayList<ImageButton> = ArrayList()
                for(xedni in 0 until 25){
                    val rowtag = "r" + xedni.toString()
                    val btn = view.findViewWithTag<ImageButton>(rowtag)
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
        Singleton.buttons = getBtns()
        val button:Button = binding.button
        val illuFLD = binding.illumination
        val tempFLD = binding.temperature


        fun inputCheck(): Boolean {
            if(illuFLD.text.toString().toDoubleOrNull() == null || tempFLD.text.toString().toDoubleOrNull() == null){
                Toast.makeText(activity,"Incorrect Input", Toast.LENGTH_SHORT).show()
                return false
            }
            if(tempFLD.text.toString().toDouble() >= 125 || tempFLD.text.toString().toDouble() <= -25){
                Toast.makeText(activity,"Incorrect Input", Toast.LENGTH_SHORT).show()
                return false
            }
            if(illuFLD.text.toString().toDouble() >= 1500 || illuFLD.text.toString().toDouble() <= 150){
                Toast.makeText(activity,"Incorrect Input", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }


        var cells:ListView = binding.cells
        cells.choiceMode = ListView.CHOICE_MODE_SINGLE
        fun modList():ArrayList<String>{
            var list = Singleton.cellList.listCells()
            var cls = vm.getCells()
            var preserve:String = ""
            for(i in cls){
                for(j in i){
                    if (j != "zeros."){
                        preserve = j
                    }
                }
            }

            if(preserve.length!=0){
                val iterator = list.iterator()
                while (iterator.hasNext()){
                    val cl = iterator.next()
                    if(cl != "zeros."  && cl != preserve){
                        iterator.remove()
                    }
                }
            }
            return list
        }
        cells.setOnItemClickListener{adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
            name = cells.getItemAtPosition(i).toString()
        }
        val arrayAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_list_item_single_choice,modList())
        cells.adapter = arrayAdapter
        cells.setItemChecked(0, true)
        name = cells.getItemAtPosition(0).toString()
        var uoc = binding.uocval
        var isc = binding.iscval
        var ff = binding.ffval
        var pmax = binding.pmaxval
        var upmax = binding.upmaxval
        var ipmax = binding.ipmaxval
        val uoctext = binding.uoc
        uoctext.text = (CircuitFragment.subscriptSpanner("UOC[V]≈",0,1,3))
        val isctext = binding.isc
        isctext.text = (CircuitFragment.subscriptSpanner("ISC[A]≈",0,1,3))
        val pmaxtext = binding.pmax
        pmaxtext.text = (CircuitFragment.subscriptSpanner("PMAX[W]≈",0,1,4))
        val umaxtext = binding.umax
        umaxtext.text = (CircuitFragment.subscriptSpanner("UPMAX[V]≈",0,1,5))
        val imaxtext = binding.imax
        imaxtext.text = (CircuitFragment.subscriptSpanner("IPMAX[A]≈",0,1,5))
        val temptext = binding.temptext
        temptext.text = (CircuitFragment.subscriptSpanner("Temperature[Co]≈",1,13,14))
        val irrtext = binding.irrText
        irrtext.text = (CircuitFragment.subscriptSpanner("Irradiance[W/m2]≈",1,14,15))

        uoc.setText(Singleton.circuit.UocMin.toString() + " V")
        isc.setText(Singleton.circuit.IscMin.toString()+ " A")
        ff.setText(Singleton.circuit.FFMin.toString())
        pmax.setText(Singleton.circuit.PmaxMin.toString() + " W")
        upmax.setText(Singleton.circuit.UmaxMin.toString() + " V")
        ipmax.setText(Singleton.circuit.ImaxMin.toString() + " A")

        button.setOnClickListener {
            if((vm.getCnter() == 2 || vm.getCnter() == 0) && inputCheck()){
                this.temp = tempFLD.text.toString().toDouble()
                this.illu = illuFLD.text.toString().toDouble()
                modCircuit(vm.getFX(),vm.getSX(),vm.getFY(),vm.getSY())
                vm.changeData(vm.getFX(),vm.getSX(),vm.getFY(),vm.getSY(),this.illu,this.temp,this.name)
                this.clrBtns()
                uoc.setText(Singleton.circuit.UocMin.toString() + " V")
                isc.setText(Singleton.circuit.IscMin.toString()+ " A")
                ff.setText(Singleton.circuit.FFMin.toString())
                pmax.setText(Singleton.circuit.PmaxMin.toString() + " W")
                upmax.setText(Singleton.circuit.UmaxMin.toString() + " V")
                ipmax.setText(Singleton.circuit.ImaxMin.toString() + " A")
                val arrayAdapter = ArrayAdapter(requireContext(),
                    android.R.layout.simple_list_item_single_choice,modList())
                cells.adapter = arrayAdapter
                cells.setItemChecked(0, true)
                name = cells.getItemAtPosition(0).toString()
            }
        }


        var toggle = binding.toggle


        var eighty:ImageButton = binding.eightyPercent
        var sixty:ImageButton = binding.sixtyPercent
        var forty:ImageButton = binding.fourtyPercent
        var twenty:ImageButton = binding.twentyPercent
        var zero:ImageButton = binding.zeroPercent

        if(vm.getToggle() == false){
            eighty.setColorFilter(Color.argb(255,255,230,0))
            sixty.setColorFilter(Color.argb(255,255,180,0))
            forty.setColorFilter(Color.argb(255,255,130,0))
            twenty.setColorFilter(Color.argb(255,255,80,0))
            zero.setColorFilter(Color.argb(255,255,30,0))
            var max = binding.max
            max.text = "125"
            var min = binding.min
            min.text = "-25"
        } else {
            eighty.setColorFilter(Color.argb(255,0,255,230))
            sixty.setColorFilter(Color.argb(255,0,255,180))
            forty.setColorFilter(Color.argb(255,0,255,130))
            twenty.setColorFilter(Color.argb(255,0,255,80))
            zero.setColorFilter(Color.argb(255,0,255,30))
            var max = binding.max
            max.text = "1500"
            var min = binding.min
            min.text = "150"
        }
        fun toggleHandle(){
            if(vm.getToggle()){
                toggle.setText("IRRAD")
            } else{
                toggle.setText("TEMP")
            }


            if(vm.getToggle()){
                eighty.setColorFilter(Color.argb(255,0,255,230))
                sixty.setColorFilter(Color.argb(255,0,255,180))
                forty.setColorFilter(Color.argb(255,0,255,130))
                twenty.setColorFilter(Color.argb(255,0,255,80))
                zero.setColorFilter(Color.argb(255,0,255,30))
                var max = binding.max
                max.text = "1500"
                var min = binding.min
                min.text = "150"
            } else {
                eighty.setColorFilter(Color.argb(255,255,230,0))
                sixty.setColorFilter(Color.argb(255,255,180,0))
                forty.setColorFilter(Color.argb(255,255,130,0))
                twenty.setColorFilter(Color.argb(255,255,80,0))
                zero.setColorFilter(Color.argb(255,255,30,0))

                var max = binding.max
                max.text = "125"
                var min = binding.min
                min.text = "-25"

            }
            clrBtns()
        }
        toggle.setOnClickListener {
            vm.toggleToggle()
            toggleHandle()
        }


        toggleHandle()
        if(vm.getCnter() == 0){
            vm.incCnter()
            vm.incCnter()
        } else if(vm.getCnter() == 1){
            vm.resCnter()
        } else{
            vm.resCnter()
            vm.incCnter()
        }
        this.markButtons(vm.getFX(),vm.getSX(),vm.getFY(),vm.getSY(),vm.getCnter())
        if(vm.getCnter() == 2){
            vm.resCnter()
        } else if(vm.getCnter() == 1){
            vm.incCnter()
        }




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
        Singleton.circuit.modify(a,b,c,d,illu,temp, name)

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
            for(row in Singleton.buttons){
                for(btn in row){
                    btn.imageAlpha = 255
                }
            }
        }
        if(vm.getCnter() ==1 || vm.getCnter() == 2){
            for(index in a until b+1){
                for(xedni in c until d+1){
                    Singleton.buttons[index][xedni].imageAlpha = 125
                }
            }
        }



    }

    fun clrBtns(){
        var matrix:ArrayList<ArrayList<Int>> = ArrayList()
        if(vm.getToggle()){
            matrix = vm.getIllu()
        } else {
            matrix = vm.getTemp()
        }
        for(row in Singleton.buttons){

            for(col in row){
                var color:Int = matrix.get(Singleton.buttons.indexOf(row)).get(row.indexOf(col))
                if(vm.getCells().get(Singleton.buttons.indexOf(row)).get(row.indexOf(col)) == "zeros."){
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
            for(row in Singleton.buttons){
                for(bt in row){
                    if (btn.equals(bt)){
                        var index = row.indexOf(bt)
                        var index2 = Singleton.buttons.indexOf(row)
                        return (Pair(index2,index))
                    }
                }

            }
            return Pair(0,0)
        }

        fun subscriptSpanner(str:String,int: Int, rangestart:Int, rangeEnd:Int): SpannableStringBuilder {
            val mStringSpan = SpannableStringBuilder(str)
            if(int == 0){
                mStringSpan.setSpan(SubscriptSpan(), rangestart, rangeEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            } else {
                mStringSpan.setSpan(SuperscriptSpan(), rangestart, rangeEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            mStringSpan.setSpan(RelativeSizeSpan(0.7f),rangestart, rangeEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return mStringSpan
        }


    }




}