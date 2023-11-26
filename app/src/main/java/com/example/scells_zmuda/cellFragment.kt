package com.example.scells_zmuda

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
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
        var illu = binding.illutxt
        var temp = binding.temptxt
        var cel: ListView = binding.cells
        var name:String = ""
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.custom_listview,helper.cellList.listCells())
        cel.adapter = arrayAdapter


        cel.setOnItemClickListener{adapterView: AdapterView<*>, view2: View, i: Int, l: Long ->
            name = cel.getItemAtPosition(i).toString()
        }

        btn.setOnClickListener {

            cird.modify(0,0,0,0,illu.text.toString().toDouble(), temp.text.toString().toDouble(),name)
            uoc.setText((round(cird.Uoc*100) /100).toString())
            isc.setText((round(cird.Isc*100) /100).toString())
            pmax.setText((round(cird.Pmax*100) /100).toString())
            upmax.setText((round(cird.Umax*100) /100).toString())
            ipmax.setText((round(cird.Imax*100) /100).toString())
            ff.setText((round(cird.FF*100) /100).toString())
        }

        return root
    }

}