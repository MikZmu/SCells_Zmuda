package com.example.scells_zmuda

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication2.Circuit
import com.example.myapplication2.Singleton
import com.example.scells_zmuda.databinding.ActivityMainBinding
import com.example.solar_cells_v3.CellList

class MainActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityMainBinding
    private val vm: circuitFragmentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var cellList: CellList = CellList(this)
        Singleton.cellList = cellList
        var circuit:Circuit = Circuit()
        Singleton.circuit = circuit
        toggle = ActionBarDrawerToggle(this, binding.drawer, binding.appbar.toolbar, R.string.drawer_open, R.string.drawer_open)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()


        binding.navigationView.setNavigationItemSelectedListener {

            if(it.itemId == R.id.nav_circuit){
                binding.appbar.title.text = "Panel Designer"
                fragmentReplacer(CircuitFragment())
            }
            if(it.itemId == R.id.nav_graph){
                binding.appbar.title.text = "Panel Data"
                fragmentReplacer(GraphFragment())
            }
            if(it.itemId == R.id.nav_cells){
                binding.appbar.title.text = "Cell Data"
                fragmentReplacer(CellFragment())

            }
            true
        }
        fragmentReplacer(CircuitFragment())
        binding.appbar.title.text = "Panel Designer"








    }

    fun fragmentReplacer(fragment: Fragment){
        var frm: FragmentManager = supportFragmentManager
        var frt:FragmentTransaction = frm.beginTransaction()
        frt.replace(R.id.frame, fragment)
        frt.commit()
    }
}


