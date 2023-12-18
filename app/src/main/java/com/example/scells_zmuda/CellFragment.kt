package com.example.scells_zmuda

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication2.Circuit
import com.example.myapplication2.Singleton
import com.example.myapplication2.Line
import com.example.scells_zmuda.databinding.FragmentCellBinding
import kotlin.math.round


class CellFragment : Fragment() {
    private var _binding: FragmentCellBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCellBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val cird:Circuit = Circuit()
        val lin:Line = Line()
        cird.lines.add(lin)
        val graph = binding.graph
        val uoc = binding.uoc
        val isc = binding.isc
        val ff = binding.ff
        val pmax = binding.pmax
        val upmax = binding.upmax
        val ipmax = binding.ipmax
        val btn = binding.btn
        val res = binding.restxt
        val illu = binding.illutxt
        val temp = binding.temptxt
        val cel: ListView = binding.cells
        val spw = binding.showpower
        val scr = binding.showcurrent
        scr.isChecked = true
        val Pop:TextView = binding.pop
        val Uop:TextView = binding.uop
        val Iop:TextView = binding.iop
        val opt:TextView = binding.optres
        val list:ArrayList<String> = Singleton.cellList.listCells()
        list.remove("zeros.")
        var name:String = list[0]
        cird.modify(0,0,0,0,illu.text.toString().toDouble(), temp.text.toString().toDouble(),name)
        val arrayAdapter = ArrayAdapter(requireContext(),
            androidx.appcompat.R.layout.select_dialog_item_material,list)
        cel.adapter = arrayAdapter
        var powerSeries = cird.minPowerPoints
        powerSeries.color = android.graphics.Color.YELLOW
        powerSeries.thickness = 4
        var currentSeries = cird.mindataPoints
        currentSeries.color = android.graphics.Color.GREEN
        currentSeries.thickness = 4
        uoc.setText((round(cird.UocMin*100) /100).toString())
        isc.setText((round(cird.IscMin*100) /100).toString())
        pmax.setText((round(cird.PmaxMin*100) /100).toString())
        upmax.setText((round(cird.UmaxMin*100) /100).toString())
        ipmax.setText((round(cird.ImaxMin*100) /100).toString())
        ff.setText((round(cird.FFMin*100) /100).toString())
        opt.setText(Circuit.round(2, (cird.UmaxMin)/(cird.ImaxMin)).toString())
        val rs:Double =res.text.toString().toDouble()
        var results = Singleton.circuit.getPower4Resistance(rs)
        val pop = results[2]
        val uop = results[0]
        val iop = results[1]
        Pop.setText(Circuit.round(2,pop).toString())
        Iop.setText(Circuit.round(2,iop).toString())
        Uop.setText(Circuit.round(2,uop).toString())
        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMaxX(cird.UocMin *1.1)
        graph.viewport.setMaxY(cird.IscMin *1.1)
        graph.gridLabelRenderer.verticalAxisTitle = "I[A]"
        graph.gridLabelRenderer.verticalAxisTitleColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.horizontalAxisTitleColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.horizontalLabelsColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.verticalLabelsColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.verticalLabelsSecondScaleColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.horizontalAxisTitle = "U[V]"
        graph.secondScale.verticalAxisTitle = "P[W]"
        graph.addSeries(currentSeries)


        val illutext = binding.illumination
        illutext.text = (CircuitFragment.subscriptSpanner("Irradiance[W/m2]≈",1,14,15))
        val temptext = binding.temperature
        temptext.text = (CircuitFragment.subscriptSpanner("Temperature[Co]≈",1,13,14))
        val uoctext = binding.uoctext
        uoctext.text = (CircuitFragment.subscriptSpanner("UOC[V]≈",0,1,3))
        val isctext = binding.isctext
        isctext.text = (CircuitFragment.subscriptSpanner("ISC[A]≈",0,1,3))
        val pmaxtext = binding.pmaxtext
        pmaxtext.text = (CircuitFragment.subscriptSpanner("PMAX[W]≈",0,1,4))
        val umaxtext = binding.upmaxtext
        umaxtext.text = (CircuitFragment.subscriptSpanner("UPMAX[V]≈",0,1,5))
        val imaxtext = binding.ipmaxtext
        imaxtext.text = (CircuitFragment.subscriptSpanner("IPMAX[A]≈",0,1,5))
        val poptext = binding.poptext
        poptext.text = (CircuitFragment.subscriptSpanner("POP[W]≈",0,1,3))
        val uoptext = binding.uoptext
        uoptext.text = (CircuitFragment.subscriptSpanner("UOP[V]≈",0,1,3))
        val ioptext = binding.ioptext
        ioptext.text = (CircuitFragment.subscriptSpanner("IOP[A]≈",0,1,3))

        fun inputCheck(): Boolean {
            if(illu.text.toString().toDoubleOrNull() == null || temp.text.toString().toDoubleOrNull() == null || res.text.toString().toDoubleOrNull() == null){
                Toast.makeText(activity,"Incorrect Input", Toast.LENGTH_SHORT).show()
                return false
            }
            if(illu.text.toString().toDouble() > 1500 || temp.text.toString().toDouble() > 125){
                Toast.makeText(activity,"Incorrect Input", Toast.LENGTH_SHORT).show()
                return false
            }
            if(illu.text.toString().toDouble() < 150 || temp.text.toString().toDouble() < -25){
                Toast.makeText(activity,"Incorrect Input", Toast.LENGTH_SHORT).show()
                return false
            }
            return true
        }

        cel.setOnItemClickListener{adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
            name = cel.getItemAtPosition(i).toString()
        }

        spw.setOnClickListener{
                if(spw.isChecked){
                    graph.viewport.isYAxisBoundsManual = true
                    graph.viewport.isXAxisBoundsManual = true
                    graph.secondScale.addSeries(powerSeries)
                    graph.secondScale.setMinY(0.0)
                    graph.viewport.setMaxX(cird.UocMin *1.1)
                    graph.secondScale.setMaxY(cird.minPower.max() * 1.1)
                } else{
                    graph.secondScale.removeSeries(powerSeries)
                }
        }

        scr.setOnClickListener{
            if (scr.isChecked){
                graph.viewport.isYAxisBoundsManual = true
                graph.viewport.isXAxisBoundsManual = true
                graph.viewport.setMaxX(cird.UocMin *1.1)
                graph.viewport.setMaxY(cird.IscMin *1.1)
                graph.addSeries(currentSeries)
            } else {
                graph.removeSeries(currentSeries)
            }
        }

        btn.setOnClickListener {
            if(inputCheck()){
                cird.modify(0,0,0,0,illu.text.toString().toDouble(), temp.text.toString().toDouble(),name)
                uoc.setText((round(cird.UocMin*100) /100).toString())
                isc.setText((round(cird.IscMin*100) /100).toString())
                pmax.setText((round(cird.PmaxMin*100) /100).toString())
                upmax.setText((round(cird.UmaxMin*100) /100).toString())
                ipmax.setText((round(cird.ImaxMin*100) /100).toString())
                ff.setText((round(cird.FFMin*100) /100).toString())
                opt.setText(Circuit.round(2, (cird.UmaxMin)/(cird.ImaxMin)).toString())
                results = Singleton.circuit.getPower4Resistance(rs)
                var pop = cird.PmaxMin
                var uop = cird.UmaxMin
                var iop = cird.ImaxMin
                Pop.setText(Circuit.round(2,pop).toString())
                Iop.setText(Circuit.round(2,iop).toString())
                Uop.setText(Circuit.round(2,uop).toString())

                powerSeries = cird.minPowerPoints
                powerSeries.setColor(Color.argb(255,255,255,0))
                powerSeries.thickness = 9
                currentSeries = cird.mindataPoints
                currentSeries.setColor(Color.argb(255,255,0,0))
                currentSeries.thickness = 9

                if (scr.isChecked){
                    graph.removeAllSeries()
                    graph.viewport.isYAxisBoundsManual = true
                    graph.viewport.isXAxisBoundsManual = true
                    graph.viewport.setMaxX(cird.UocMin *1.1)
                    graph.viewport.setMaxY(cird.IscMin *1.1)
                    graph.addSeries(currentSeries)
                } else {
                    graph.removeSeries(currentSeries)
                }

                spw.setOnClickListener{
                    if(spw.isChecked){
                        graph.secondScale.removeAllSeries()
                        graph.viewport.isYAxisBoundsManual = true
                        graph.viewport.isXAxisBoundsManual = true
                        graph.secondScale.addSeries(powerSeries)
                        graph.secondScale.setMinY(0.0)
                        graph.viewport.setMaxX(cird.UocMin *1.1)
                        graph.secondScale.setMaxY(cird.minPower.max() * 1.1)
                    } else{
                        graph.secondScale.removeSeries(powerSeries)
                    }
                }
            }



        }

        return root
    }

}