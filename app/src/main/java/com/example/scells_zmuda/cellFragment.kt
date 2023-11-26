package com.example.scells_zmuda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.myapplication2.circuit
import com.example.myapplication2.helper
import com.example.myapplication2.line
import com.example.scells_zmuda.databinding.FragmentCellBinding
import com.example.scells_zmuda.databinding.FragmentCircuitBinding
import kotlin.math.round


class cellFragment : Fragment() {
    private var _binding: FragmentCellBinding? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCellBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var cird:circuit = circuit()
        var lin:line = line()
        cird.lines.add(lin)
        var graph = binding.graph
        var uoc = binding.uoc
        var isc = binding.isc
        var ff = binding.ff
        var pmax = binding.pmax
        var upmax = binding.upmax
        var ipmax = binding.ipmax
        var btn = binding.btn
        var res = binding.restxt
        var illu = binding.illutxt
        var temp = binding.temptxt
        var cel: ListView = binding.cells
        var spw = binding.showpower
        var scr = binding.showcurrent
        var Pop:TextView = binding.pop
        var Uop:TextView = binding.uop
        var Iop:TextView = binding.iop
        var opt:TextView = binding.optres
        var list:ArrayList<String> = helper.cellList.listCells()
        list.remove("zeros.")
        var name:String = list[0]
        cird.modify(0,0,0,0,illu.text.toString().toDouble(), temp.text.toString().toDouble(),name)
        val arrayAdapter = ArrayAdapter(requireContext(),
            androidx.appcompat.R.layout.select_dialog_item_material,list)
        cel.adapter = arrayAdapter
        var powerSeries = cird.powerPoints
        var currentSeries = cird.dataPoints
        uoc.setText((round(cird.Uoc*100) /100).toString())
        isc.setText((round(cird.Isc*100) /100).toString())
        pmax.setText((round(cird.Pmax*100) /100).toString())
        upmax.setText((round(cird.Umax*100) /100).toString())
        ipmax.setText((round(cird.Imax*100) /100).toString())
        ff.setText((round(cird.FF*100) /100).toString())
        opt.setText(circuit.round(2, (cird.Umax)/(cird.Imax)).toString())
        var rs:Double =res.text.toString().toDouble()
        var results = helper.circuit.getPower4Resistance(rs)
        var pop = results[2]
        var uop = results[0]
        var iop = results[1]
        Pop.setText(circuit.round(2,pop).toString())
        Iop.setText(circuit.round(2,iop).toString())
        Uop.setText(circuit.round(2,uop).toString())


        cel.setOnItemClickListener{adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
            name = cel.getItemAtPosition(i).toString()
        }

        spw.setOnClickListener{
                if(spw.isChecked){
                    graph.viewport.isYAxisBoundsManual = true
                    graph.viewport.isXAxisBoundsManual = true
                    graph.secondScale.addSeries(powerSeries)
                    graph.secondScale.setMinY(0.0)
                    graph.viewport.setMaxX(cird.Uoc *1.1)
                    graph.secondScale.setMaxY(cird.power.max() * 1.1)
                } else{
                    graph.secondScale.removeSeries(powerSeries)
                }
        }

        scr.setOnClickListener{
            if (scr.isChecked){
                graph.viewport.isYAxisBoundsManual = true
                graph.viewport.isXAxisBoundsManual = true
                graph.viewport.setMaxX(cird.Uoc *1.1)
                graph.viewport.setMaxY(cird.Isc *1.1)
                graph.addSeries(currentSeries)
            } else {
                graph.removeSeries(currentSeries)
            }
        }

        btn.setOnClickListener {
            cird.modify(0,0,0,0,illu.text.toString().toDouble(), temp.text.toString().toDouble(),name)
            uoc.setText((round(cird.Uoc*100) /100).toString())
            isc.setText((round(cird.Isc*100) /100).toString())
            pmax.setText((round(cird.Pmax*100) /100).toString())
            upmax.setText((round(cird.Umax*100) /100).toString())
            ipmax.setText((round(cird.Imax*100) /100).toString())
            ff.setText((round(cird.FF*100) /100).toString())
            opt.setText(circuit.round(2, (cird.Umax)/(cird.Imax)).toString())
            results = helper.circuit.getPower4Resistance(rs)
            var pop = results[2]
            var uop = results[0]
            var iop = results[1]
            Pop.setText(circuit.round(2,pop).toString())
            Iop.setText(circuit.round(2,iop).toString())
            Uop.setText(circuit.round(2,uop).toString())

            powerSeries = cird.powerPoints
            currentSeries = cird.dataPoints
            if (scr.isChecked){
                graph.removeAllSeries()
                graph.viewport.isYAxisBoundsManual = true
                graph.viewport.isXAxisBoundsManual = true
                graph.viewport.setMaxX(cird.Uoc *1.1)
                graph.viewport.setMaxY(cird.Isc *1.1)
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
                    graph.viewport.setMaxX(cird.Uoc *1.1)
                    graph.secondScale.setMaxY(cird.power.max() * 1.1)
                } else{
                    graph.secondScale.removeSeries(powerSeries)
                }
            }


        }

        return root
    }

}