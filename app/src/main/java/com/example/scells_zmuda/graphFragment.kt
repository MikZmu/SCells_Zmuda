package com.example.scells_zmuda

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
import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.Color
import com.example.myapplication2.circuit
import com.example.myapplication2.helper
import com.example.myapplication2.line
import com.example.scells_zmuda.databinding.FragmentCircuitBinding
import com.example.scells_zmuda.databinding.FragmentGraphBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.round


class graphFragment : Fragment() {
    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {








        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var graph:GraphView = binding.graph
        var uoc:TextView = binding.uoc
        var isc:TextView = binding.isc
        var pmax:TextView = binding.pmax
        var Upmax:TextView = binding.umax
        var Imax:TextView = binding.imax
        var ff:TextView = binding.ff
        var calc:Button = binding.calculate
        var res = binding.resistance
        var Uop = binding.uop
        var Iop = binding.iop
        var Pop = binding.pop
        var spw = binding.showpower
        var scr = binding.showcurrent
        var sls = binding.perfectCircuit

        var currentSeries:LineGraphSeries<DataPoint> = helper.circuit.dataPoints
        currentSeries.thickness = 3
        currentSeries.setColor(android.graphics.Color.RED)

        var powerSeries:LineGraphSeries<DataPoint> = helper.circuit.powerPoints
        powerSeries.thickness = 3
        powerSeries.setColor(android.graphics.Color.YELLOW)

        var idealSeries:LineGraphSeries<DataPoint> = helper.circuit.perfectDP

        scr.setOnClickListener {
            if(scr.isChecked){
                if(sls.isChecked){
                    graph.addSeries(currentSeries)
                } else {
                    graph.viewport.isYAxisBoundsManual = true
                    graph.viewport.isXAxisBoundsManual = true
                    graph.viewport.setMaxX(helper.circuit.Uoc *1.1)
                    graph.viewport.setMaxY(helper.circuit.Isc *1.1)
                    graph.addSeries(currentSeries)
                }

            } else{
                graph.removeSeries(currentSeries)
            }
        }

        spw.setOnClickListener{
            if(spw.isChecked){
                graph.secondScale.addSeries(powerSeries)
                graph.secondScale.setMinY(0.0)
                graph.secondScale.setMaxY(helper.circuit.power.max() * 1.1)
            } else{
                graph.secondScale.removeAllSeries()
            }
        }

        sls.setOnClickListener{
            if(sls.isChecked){
                graph.viewport.isYAxisBoundsManual = true
                graph.viewport.isXAxisBoundsManual = true
                graph.viewport.setMaxX(helper.circuit.perfectGridX.max() *1.1)
                graph.viewport.setMaxY(helper.circuit.perfectSum.max() *1.1)
                graph.addSeries(idealSeries)
            } else{
                graph.removeSeries(idealSeries)
                if(scr.isChecked){
                    graph.viewport.isYAxisBoundsManual = true
                    graph.viewport.isXAxisBoundsManual = true
                    graph.viewport.setMaxX(helper.circuit.Uoc *1.1)
                    graph.viewport.setMaxY(helper.circuit.Isc *1.1)
                }
            }
        }

        calc.setOnClickListener{
            var rs:Double =res.text.toString().toDouble()
            var results = helper.circuit.getPower4Resistance(rs)
            var pop = results[2]
            var uop = results[0]
            var iop = results[1]
            Pop.setText(pop.toString())
            Iop.setText(iop.toString())
            Uop.setText(uop.toString())
        }

        uoc.setText(circuit.round(2,helper.circuit.Uoc).toString())
        isc.setText((round(helper.circuit.Isc*100)/100).toString())
        pmax.setText((round(helper.circuit.Pmax*100)/100).toString())
        Upmax.setText((round(helper.circuit.Umax*100)/100).toString())
        Imax.setText((round(helper.circuit.Imax*100)/100).toString())
        ff.setText((round(helper.circuit.FF*100)/100).toString())

        return root
    }
}










