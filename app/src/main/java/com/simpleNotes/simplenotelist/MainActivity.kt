package com.simpleNotes.simplenotelist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.simpleNotes.simplenotelist.controller.NavVisibilityController
import com.simpleNotes.simplenotelist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MAIN = this
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        navController = Navigation.findNavController(this,R.id.nav_fragment)

        binding.bottomNavigationView.setupWithNavController(navController)

        val navigationVisibilityController = NavVisibilityController(binding.bottomNavigationView)
        navController.addOnDestinationChangedListener(navigationVisibilityController)

//        initToolbar()
    }

//    private fun initToolbar() {
//        (MAIN as MenuHost).addMenuProvider(object: MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.popup_menu_notes, menu)
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                when(menuItem.itemId){
//                    R.id.popup_menu_item_settings -> {
//                        MAIN.navController.navigate(R.id.nav_bar_item_settings)
//                        return true
//                    }
//                    R.id.popup_menu_item_contactUs -> {
//                        return true
//                    }
//                }
//                return false
//            }
//        })
//    }

//    fun setActionBarTitle(titleResourceId: Int) {
//        supportActionBar!!.title = getString(titleResourceId)
//    }






//    private fun replaceFragment(fragment: Fragment){
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.nav_fragment,fragment)
//        fragmentTransaction.commit()
//
//    }
}