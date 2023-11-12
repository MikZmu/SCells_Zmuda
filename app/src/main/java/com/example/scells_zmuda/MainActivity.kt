package com.example.scells_zmuda

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.myapplication2.circuit
import com.example.myapplication2.helper
import com.example.myapplication2.line
import com.example.scells_zmuda.databinding.ActivityMainBinding
import com.example.scells_zmuda.ui.theme.SCells_ZmudaTheme
import com.example.solar_cells_v3.cellList
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setSupportActionBar(binding.appbar.toolbar)
        var cellList: cellList = cellList(this)
        helper.cellList = cellList
        var line1: line = line()
        var line2: line = line()
        var line3: line = line()

        var circuit:circuit = circuit()
        circuit.lines.add(line1)
        circuit.lines.add(line2)
        circuit.lines.add(line3)
        helper.circuit = circuit


        toggle = ActionBarDrawerToggle(this, binding.drawer, binding.appbar.toolbar, R.string.drawer_open, R.string.drawer_open)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_circuit -> fragmentReplacer(circuitFragment())
                R.id.nav_graph -> fragmentReplacer(graphFragment())
                R.id.nav_cells -> fragmentReplacer(cellFragment())
            }
            true

        }





    }

    fun fragmentReplacer(fragment: Fragment){
        var frm: FragmentManager = supportFragmentManager
        var frt:FragmentTransaction = frm.beginTransaction()
        frt.replace(R.id.frame, fragment)
        frt.commit()
    }
}


