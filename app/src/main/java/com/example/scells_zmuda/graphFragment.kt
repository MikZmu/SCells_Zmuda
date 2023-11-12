package com.example.scells_zmuda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication2.circuit
import com.example.myapplication2.helper
import com.example.myapplication2.line
import com.example.scells_zmuda.databinding.FragmentCircuitBinding
import com.example.scells_zmuda.databinding.FragmentGraphBinding
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries


class graphFragment : Fragment() {
    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment







        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var graph:GraphView = binding.graph
        var series:LineGraphSeries<DataPoint?> = helper.circuit.dataPoints
        graph.addSeries(series)
        return inflater.inflate(R.layout.fragment_graph, container, false)
    }
}





