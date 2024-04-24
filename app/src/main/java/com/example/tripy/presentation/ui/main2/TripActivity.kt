package com.example.tripy.presentation.ui.main2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.tripy.R
import com.example.tripy.databinding.ActivityTripBinding
import com.example.tripy.presentation.ui.main.HomeActivity
import com.example.tripy.room.TripTable
import com.example.tripy.utils.AlertDialogHelper
import com.example.tripy.utils.Constants
import com.example.tripy.viewmodels.ExpenseViewModel
import com.example.tripy.viewmodels.TripViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TripActivity : AppCompatActivity() {

    private var currentFragment: Fragment? = null
    private var _binding: ActivityTripBinding? = null
    private val binding get() = _binding!!
    private lateinit var editTripFragment: EditTripFragment
    private lateinit var expenseFragment: ExpenseFragment
    private lateinit var tripDetailFragment: TripDetailFragment
    private lateinit var currentTrip: TripTable


    private val viewModel by viewModels<TripViewModel>()
    private val expenseViewModel by viewModels<ExpenseViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTripFragment = EditTripFragment()
        expenseFragment = ExpenseFragment()
        tripDetailFragment = TripDetailFragment()

        val receivedBundle = intent.extras

        val receiveTrip: TripTable? =
            receivedBundle?.getParcelable(Constants.ARG_SELECTED_TRIP)
        if (receiveTrip != null) {
            currentTrip = receiveTrip
        }

        if (savedInstanceState == null) {
            replaceFragment(tripDetailFragment)
            currentFragment = tripDetailFragment
        } else {
            currentFragment = supportFragmentManager.findFragmentById(R.id.container)
        }
        //    binding.bottomNavigation.selectedItemId=0
        currentFragment?.let {
            binding.bottomNavigation.selectedItemId = it.id
        }
        Log.e("oncreate-->", "i am here ")

        setupBottomNavigationBar()
    }

    override fun onResume() {
        super.onResume()
        Log.d("trip activity", "onResume()")
        if (currentFragment == tripDetailFragment) {
            binding.bottomNavigation.visibility = View.VISIBLE
        } else {
            binding.bottomNavigation.visibility = View.GONE
        }
    }


    private fun setupBottomNavigationBar() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val newFragment = when (item.itemId) {
                R.id.expense -> expenseFragment
                R.id.editTrip -> editTripFragment
                R.id.delete -> {
                    AlertDialogHelper.showAlertDialog(
                        this,
                        "Delete TRIP?",
                        R.drawable.delete,
                        "DELETE",
                        "CANCEL",
                        {
                            lifecycleScope.launch {
                                viewModel.deleteTrip(currentTrip)
                                expenseViewModel.deleteExpenses(currentTrip.id)
                            }
                            val intent = Intent(this, HomeActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    ) {
                        // replaceFragment(tripDetailFragment)
                    }
                    currentFragment
                }
                else -> tripDetailFragment
            }

            if (newFragment != currentFragment) {
                replaceFragment(newFragment as Fragment)
                currentFragment = newFragment
                if (currentFragment != tripDetailFragment) {
                    binding.bottomNavigation.visibility = View.GONE
                }

                Log.d("current frag", currentFragment.toString())
            }

            true
        }
    }



//    private fun replaceFragment(fragment: Fragment) {
//
//        if (fragment == tripDetailFragment) {
//            Log.e("trip activity->", "bottom bar is visible  ")
//            binding.bottomNavigation.visibility = View.VISIBLE
//        }
//        val bundle = Bundle().apply {
//            putParcelable(Constants.ARG_SELECTED_TRIP, currentTrip)
//        }
//
//        fragment.arguments = bundle
//
//        val transaction = supportFragmentManager.beginTransaction()
//            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
//
//        transaction.replace(R.id.container, fragment)
//        if (fragment==expenseFragment){
//           //supportFragmentManager.popBackStack()
//        }
//        else{
//           // transaction.addToBackStack(null)
//
//        }
//        transaction.commit()
//
//        currentFragment = fragment // Update current fragment
////        binding.bottomNavigation.selectedItemId = when (fragment) {
////            expenseFragment -> R.id.expense
////            editTripFragment -> R.id.editTrip
////            else -> 0
////        }
//    }


    private fun replaceFragment(fragment: Fragment) {

        if (fragment == tripDetailFragment) {
            Log.e("trip activity->", "bottom bar is visible  ")
            binding.bottomNavigation.visibility = View.VISIBLE
        }
        val bundle = Bundle().apply {
            putParcelable(Constants.ARG_SELECTED_TRIP, currentTrip)
        }

        fragment.arguments = bundle

        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in, R.anim.slide_out)
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

        currentFragment = fragment // Update current fragment
//        binding.bottomNavigation.selectedItemId = when (fragment) {
//            expenseFragment -> R.id.expense
//            editTripFragment -> R.id.editTrip
//            else -> 0
//        }
    }


    //    override fun onBackPressed() {
//        if (currentFragment == expenseFragment || currentFragment == editTripFragment) {
//
//            replaceFragment(tripDetailFragment)
//            currentFragment = tripDetailFragment
//        } else
//            if (currentFragment == tripDetailFragment) {
//            finish()
//        }
//            else if (supportFragmentManager.backStackEntryCount > 0) {
//
//            supportFragmentManager.popBackStack()
//            currentFragment = supportFragmentManager.findFragmentById(R.id.fragContainer)
//        }
//            else {
//
//            super.onBackPressed()
//        }
//    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        Log.d("trip activity","enter onBackPressed")
        when {
            currentFragment == expenseFragment -> {// || currentFragment == editTripFragment
                replaceFragment(tripDetailFragment)

            }

            currentFragment==editTripFragment ->{
                if (editTripFragment.backPressedCallback.isEnabled) {

                    editTripFragment.backPressedCallback.handleOnBackPressed()
                    return
                }
            }

            currentFragment == tripDetailFragment -> {
                finish()
            }

            supportFragmentManager.backStackEntryCount > 0 -> {
                supportFragmentManager.popBackStack()
                currentFragment = supportFragmentManager.findFragmentById(R.id.container)
            }

            else -> super.onBackPressed()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun showBottomNavigationBar() {
        // Show the bottom navigation bar
        binding.bottomNavigation.visibility = View.VISIBLE

        currentFragment = tripDetailFragment
    }
    fun disableBottomNavigationBar(){
        binding.bottomNavigation.visibility=View.GONE
    }


}