package com.ilkeryildirim.cryptracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ilkeryildirim.cryptracker.databinding.ActivityMainBinding
import com.ilkeryildirim.cryptracker.utils.gone
import com.ilkeryildirim.cryptracker.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = findNavController(R.id.hostFragment)
        setupBottomNavigation()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.bottomNavigation.gone()
            //   if(destination.id==R.id.splashFragment || destination.id==R.id.registerFragment|| destination.id==R.id.loginFragment)  binding.bottomNavigation.gone() else binding.bottomNavigation.visible()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun setupBottomNavigation() {
       binding.bottomNavigation.setupWithNavController(navController)

    }

}