package com.example.tripy.presentation.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tripy.R
import com.example.tripy.databinding.ActivityHomeBinding
import com.example.tripy.utils.AlertDialogHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var currentFragment: Fragment? = null
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainFragment: MainFragment
    private lateinit var addTripFragment: AddTripFragment
    private lateinit var checklistFragment: ChecklistFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainFragment = MainFragment()
        addTripFragment = AddTripFragment()
        checklistFragment = ChecklistFragment()

        if (savedInstanceState == null) {
            replaceFragment(mainFragment)
            currentFragment = mainFragment
        }


        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val newFragment = when (item.itemId) {
                R.id.addTrip -> addTripFragment
                R.id.checkList -> checklistFragment
              R.id.main-> {
                  navigateToWeb()
                  currentFragment
              }
                else -> mainFragment
            }
            if (newFragment != currentFragment) {
                replaceFragment(newFragment as Fragment)
                currentFragment = newFragment
            }
            true
        }
    }

    private fun navigateToWeb() {

        AlertDialogHelper.showAlertDialog(
            this,
            "Check Weather Condition",
            null,
            "Go",
            "Cancel",
            {


                val websiteUri = Uri.parse("https://www.accuweather.com/")
                val intent = Intent(Intent.ACTION_VIEW, websiteUri)
                startActivity(intent)

            }
        ) {}

    }

    private fun replaceFragment(fragment: Fragment) {

//        if (fragment != mainFragment) {
//            disableBottomNavigationBar()
//        } else {
//            showBottomNavigationBar()
//        }
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()

        // Update selected item in bottom navigation view
        currentFragment = fragment
//        when (fragment) {
//            is AddTripFragment -> binding.bottomNavigation.selectedItemId = R.id.addTrip
//            is ChecklistFragment -> binding.bottomNavigation.selectedItemId = R.id.checkList
//            is MainFragment -> binding.bottomNavigation.selectedItemId = R.id.main
//            else -> binding.bottomNavigation.selectedItemId = R.id.main
//        }
        Log.d("home activity-->", "inside when block")
    }


//    override fun onBackPressed() {
//        // here if mainfragment then only exit
//        if (currentFragment is MainFragment) {
//            finish()
//        } else {
//            if (supportFragmentManager.backStackEntryCount > 0) {
//                supportFragmentManager.popBackStack()
//            } else {
//                super.onBackPressed()
//            }
//        }
//    }

    override fun onBackPressed() {

        if (currentFragment is MainFragment){
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            finish()
        }
        else if (currentFragment !is MainFragment) {
           // binding.bottomNavigation.selectedItemId = R.id.main
            replaceFragment(mainFragment)
            currentFragment = mainFragment
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.addOnBackStackChangedListener(backStackListener)
    }

    override fun onPause() {
        super.onPause()
        supportFragmentManager.removeOnBackStackChangedListener(backStackListener)
    }

    private val backStackListener = FragmentManager.OnBackStackChangedListener {
        currentFragment = supportFragmentManager.findFragmentById(R.id.container)
    }

    fun showBottomNavigationBar() {
        // Show the bottom navigation bar
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    fun disableBottomNavigationBar() {
        binding.bottomNavigation.visibility = View.GONE
    }
}