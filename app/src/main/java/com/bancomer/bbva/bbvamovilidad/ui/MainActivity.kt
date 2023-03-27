package com.bancomer.bbva.bbvamovilidad.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bancomer.bbva.bbvamovilidad.R
import com.bancomer.bbva.bbvamovilidad.databinding.ActivityMainBinding
import com.bancomer.bbva.bbvamovilidad.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()

    }
    private fun setUpNavigation() {
        supportActionBar?.hide()
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.ic_home ->{
                    navController.popBackStack()
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.ic_new_trip ->{
                    navController.popBackStack()
                    navController.navigate(R.id.newTripFragment)
                    true
                }
                R.id.ic_comparative ->{
                    navController.popBackStack()
                    navController.navigate(R.id.comparativeFragment)
                    true
                }
                R.id.ic_record ->{
                    navController.popBackStack()
                    navController.navigate(R.id.itemFragment)
                    true
                }
//                R.id.ic_more ->{
//                    navController.popBackStack()
//                    navController.navigate(R.id.moreFragment)
//                    true
//                }
                else -> {false}
            }
        }
    }
}