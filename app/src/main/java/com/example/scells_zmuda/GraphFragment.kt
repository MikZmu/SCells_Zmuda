package com.example.scells_zmuda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.myapplication2.Circuit
import com.example.myapplication2.Singleton
import com.example.scells_zmuda.databinding.FragmentGraphBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.round


class GraphFragment : Fragment() {
    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {








        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val graph:GraphView = binding.graph
        val uoc:TextView = binding.uoc
        val isc:TextView = binding.isc
        val pmax:TextView = binding.pmax
        val Upmax:TextView = binding.umax
        val Imax:TextView = binding.imax
        var ff:TextView = binding.ff
        var calc:Button = binding.calculate
        var res = binding.resistance
        var Uop = binding.uop
        var Iop = binding.iop
        var Pop = binding.pop
        var spw = binding.showpower
        var scr = binding.showcurrent
        var sls = binding.perfectCircuit
        var optres:TextView = binding.opt
        var smp = binding.potPower
        val uoctext = binding.uoctext
        uoctext.text = (CircuitFragment.subscriptSpanner("UOC[V]≈",0,1,3))
        val isctext = binding.isctext
        isctext.text = (CircuitFragment.subscriptSpanner("ISC[A]≈",0,1,3))
        val pmaxtext = binding.pmaxtext
        pmaxtext.text = (CircuitFragment.subscriptSpanner("PMAX[W]≈",0,1,4))
        val upmaxtext = binding.upmaxtext
        upmaxtext.text = (CircuitFragment.subscriptSpanner("UPMAX[V]≈",0,1,5))
        val ipmaxtext = binding.ipmaxtext
        ipmaxtext.text = (CircuitFragment.subscriptSpanner("IPMAX[A]≈",0,1,5))
        val poptext = binding.poptext
        poptext.text = (CircuitFragment.subscriptSpanner("POP[W]≈",0,1,3))
        val uoptext = binding.uoptext
        uoptext.text = (CircuitFragment.subscriptSpanner("UOP[V]≈",0,1,3))
        val ioptext = binding.ioptext
        ioptext.text = (CircuitFragment.subscriptSpanner("IOP[A]≈",0,1,3))

        var currentSeries:LineGraphSeries<DataPoint> = Singleton.circuit.mindataPoints
        currentSeries.thickness = 3
        currentSeries.setColor(android.graphics.Color.RED)
        currentSeries.title = "real I = f(U)"

        var powerSeries:LineGraphSeries<DataPoint> = Singleton.circuit.minPowerPoints
        powerSeries.thickness = 3
        powerSeries.setColor(android.graphics.Color.YELLOW)
        powerSeries.title = "real P = f(U)"

        var idealSeries:LineGraphSeries<DataPoint> = Singleton.circuit.maxDataPoints

        idealSeries.thickness = 3
        idealSeries.setColor(android.graphics.Color.CYAN)
        idealSeries.title = "potential I = f(U)"

        var maxPowerSeries:LineGraphSeries<DataPoint> = Singleton.circuit.maxPowerPoints

        maxPowerSeries.thickness = 3
        maxPowerSeries.setColor(android.graphics.Color.GREEN)
        maxPowerSeries.title = "potential P = f(U)"

        var seriesArray:ArrayList<LineGraphSeries<DataPoint>> = ArrayList()
        seriesArray.add(currentSeries)
        seriesArray.add(idealSeries)
        seriesArray.add(powerSeries)
        seriesArray.add(maxPowerSeries)
        var maxSeries:ArrayList<Double> = ArrayList()
        maxSeries.add(Singleton.circuit.minGridX.getOrElse(Singleton.circuit.minGridX.size-1){0.0})

        maxSeries.add(Singleton.circuit.maxGridX.getOrElse(Singleton.circuit.maxGridX.size-1){0.0})

        maxSeries.add(Singleton.circuit.minSum.getOrElse(0){0.0})
        maxSeries.add(Singleton.circuit.maxSum.getOrElse(0){0.0})
        try{maxSeries.add(Singleton.circuit.minPower.max())} catch (e: NoSuchElementException) {maxSeries.add(0.0)}
        try{maxSeries.add(Singleton.circuit.maxPower.max())} catch (e: NoSuchElementException) {maxSeries.add(0.0)}
        var maskX:ArrayList<Int> = ArrayList()
        maskX.add(0)
        maskX.add(0)
        var maskYC:ArrayList<Int> = ArrayList()
        maskYC.add(0)
        maskYC.add(0)
        var maskYP:ArrayList<Int> = ArrayList()
        maskYP.add(0)
        maskYP.add(0)
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.gridLabelRenderer.verticalAxisTitle = "I[A]"
        graph.gridLabelRenderer.verticalAxisTitleColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.horizontalAxisTitleColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.horizontalLabelsColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.verticalLabelsColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.verticalLabelsSecondScaleColor = android.graphics.Color.YELLOW
        graph.gridLabelRenderer.horizontalAxisTitle = "U[V]"
        graph.secondScale.verticalAxisTitle = "P[W]"
        scr.isChecked = true


        fun findHighestX(): Double {
            var highest = 0.0;
            for(i in 0..1){
                if(maskX[i]!= 0){
                    if(maxSeries[i]>highest){
                        highest = maxSeries[i]
                    }
                }
            }
            if(highest == 0.0){
                return 1.0
            }
            return (highest * 1.1)
        }

        fun findHighestYC():Double{
            var highest = 0.0;
            for(i in 0..1){
                if(maskYC[i] != 0){
                    if(maxSeries[i+2]>highest){
                        highest = maxSeries[i+2]
                    }
                }
            }
            if(highest == 0.0){
                return 1.0
            }
            return (highest * 1.1)
        }

        fun findHighestYP():Double{
            var highest = 0.0;
            for(i in 0..1){
                if(maskYP[i] != 0){
                    if(maxSeries[i+4]>highest){
                        highest = maxSeries[i+4]
                    }
                }
            }
            if(highest == 0.0){
                return 0.0
            }
            return (highest * 1.1)
        }




        fun scrHandle(){
            if(scr.isChecked){
                maskX[0] = 1
                maskYC[0] = 1
                graph.addSeries(currentSeries)
            } else{
                if(spw.isChecked == false){
                    maskX[0] = 0
                }
                maskYC[0] = 0
                graph.removeSeries(currentSeries)
            }
            graph.viewport.setMinX(0.0)
            graph.viewport.setMaxX(findHighestX())
            graph.viewport.setMaxY(findHighestYC())
        }


        scr.setOnClickListener {
            scrHandle()
        }


        fun slsHandle(){
            if(sls.isChecked){
                maskX[1] = 1
                maskYC[1] = 1
                graph.addSeries(idealSeries)
            } else{
                if(smp.isChecked == false){
                    maskX[1] = 0
                }
                maskYC[1] = 0
                graph.removeSeries(idealSeries)
            }
            graph.viewport.setMaxX(findHighestX())
            graph.viewport.setMaxY(findHighestYC())
        }


        sls.setOnClickListener{
            slsHandle()
        }

        fun smpHandle(){
            if(smp.isChecked){
                maskX[1] = 1
                maskYP[1] = 1
                graph.secondScale.addSeries(maxPowerSeries)
            } else{
                if(sls.isChecked == false){
                    maskX[1] = 0
                }
                maskYP[1] = 0
                graph.secondScale.removeSeries(maxPowerSeries)
            }
            graph.viewport.setMaxX(findHighestX())
            graph.secondScale.setMinY(0.0);
            graph.secondScale.setMaxY(findHighestYP())
        }



        smp.setOnClickListener{
            smpHandle()
        }

        fun spwHandle(){
            if(spw.isChecked){
                maskX[0] = 1
                maskYP[0] = 1
                graph.secondScale.addSeries(powerSeries)
            } else{
                if(scr.isChecked == false){
                    maskX[0] = 0
                }
                maskYP[0] = 0
                graph.secondScale.removeSeries(powerSeries)
            }
            graph.viewport.setMaxX(findHighestX())
            graph.secondScale.setMinY(0.0);
            graph.secondScale.setMaxY(findHighestYP())
        }


        spw.setOnClickListener{
            spwHandle()
        }




        calc.setOnClickListener{
            var rs:Double =res.text.toString().toDouble()
            var results = Singleton.circuit.getPower4Resistance(rs)
            var pop = results[2]
            var uop = results[0]
            var iop = results[1]
            Pop.setText(Circuit.round(2,pop).toString())
            Iop.setText(Circuit.round(2,iop).toString())
            Uop.setText(Circuit.round(2,uop).toString())
        }



        uoc.setText(Circuit.round(2,Singleton.circuit.UocMin).toString())
        isc.setText((round(Singleton.circuit.IscMin*100)/100).toString())
        pmax.setText((round(Singleton.circuit.PmaxMin*100)/100).toString())
        Upmax.setText((round(Singleton.circuit.UmaxMin*100)/100).toString())
        Imax.setText((round(Singleton.circuit.ImaxMin*100)/100).toString())
        ff.setText((round(Singleton.circuit.FFMin*100)/100).toString())
        optres.setText(Circuit.round(2, (Singleton.circuit.UmaxMin)/(Singleton.circuit.ImaxMin)).toString())
        res.setText(Singleton.circuit.charResMin.toString())

        scrHandle()


        return root
    }
}










