package com.example.tripy.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tripy.R
import com.example.tripy.databinding.FragmentAddTripBinding
import com.example.tripy.databinding.ToolBarBinding
import com.example.tripy.room.TripTable
import com.example.tripy.utils.showCurrencyDialog
import com.example.tripy.utils.showDatePicker
import com.example.tripy.viewmodels.TripViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddTripFragment : Fragment() {

    private var _binding: FragmentAddTripBinding? = null
    private val binding get() = _binding!!

    private lateinit var startDate: String
    private lateinit var endDate: String

    private var _toolbarBinding: ToolBarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private val viewModel by viewModels<TripViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddTripBinding.inflate(inflater)
        _toolbarBinding = ToolBarBinding.bind(binding.toolbar.root)
      //  (requireActivity() as HomeActivity).setSupportActionBar(toolbarBinding.toolbar)
        binding.calendar.setOnClickListener {

            //calendar()
binding.calendar.isEnabled=false


            showDatePicker(
                requireActivity(),
                requireActivity().supportFragmentManager
            ) { startDate, endDate ->
                binding.selectedDate.text = "$startDate - $endDate"
                binding.selectedDate.visibility = View.VISIBLE
                this.startDate = startDate
                this.endDate = endDate
                binding.calendar.isEnabled=true
            }
        }

        binding.currency.setOnClickListener {
            showCurrencyDialog(requireActivity()) { selectedCurrency ->
                if (selectedCurrency != null) {
                    binding.selectedCurr.text = selectedCurrency
                    binding.selectedCurr.visibility = View.VISIBLE
                }
            }
        }

        binding.saveTripBtn.setOnClickListener {

//            navigateToMainFragment()
            if (validation()) {
                onButtonClick()
            } else {
                Toast.makeText(
                    requireActivity(),
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }

            //     setupWarningVisibility(binding.tripName, binding.tripNameWarning)
            //     setupWarningVisibility(binding.tripLocation, binding.tripLocationWarning)
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbarBinding.title.text = "Add Trip"
        toolbarBinding.icon.setOnClickListener {
            navigateToMainFragment()
        }
//        toolbarBinding.toolbar.setNavigationOnClickListener {
//            requireActivity().onBackPressed()
//        }
    }

    override fun onResume() {
        super.onResume()
     (activity as HomeActivity?)?.disableBottomNavigationBar()
    }

    private fun onButtonClick() {
        val tripName = binding.tripName.text.toString()
        val tripLocation = binding.tripLocation.text.toString()
        val currency = binding.selectedCurr.text.toString()

        val trip = TripTable(
            tripName = tripName,
            tripLocation = tripLocation,
            startDate = startDate,
            endDate = endDate,
            currency = currency
        )
        //  binding.tripName.text =""

        viewModel.insertTrip(trip)

        binding.tripName.text = null
        binding.tripLocation.text = null
        binding.selectedCurr.text = null
        binding.selectedDate.text = null


        navigateToMainFragment()
    }


//    private fun navigateToMainFragment() {
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.fragContainer, MainFragment())
//            .addToBackStack(null)
//            .commit()
//    }
//

    private fun navigateToMainFragment() {
//        requireActivity().supportFragmentManager.popBackStack(null,
//            FragmentManager.POP_BACK_STACK_INCLUSIVE)
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.fragContainer, MainFragment())
//            .commit()

        val intent = Intent(requireActivity(), HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in,R.anim.slide_out)
    }



    private fun validation(): Boolean {
        binding.tripNameWarning.visibility = View.GONE
        binding.tripLocationWarning.visibility = View.GONE
        binding.calendarWarning.visibility = View.GONE
        binding.currencyWarning.visibility = View.GONE

        var isValid = true

        if (binding.tripName.text?.isBlank() == true) {
            binding.tripNameWarning.visibility = View.VISIBLE
            isValid = false
        }
        if (binding.tripLocation.text?.isBlank() == true) {
            binding.tripLocationWarning.visibility = View.VISIBLE
            isValid = false
        }
        if (binding.selectedDate.text.isBlank()) {
            binding.calendarWarning.visibility = View.VISIBLE
            isValid = false
        }
        if (binding.selectedCurr.text.isBlank()) {
            binding.currencyWarning.visibility = View.VISIBLE
            isValid = false
        }

        return isValid
    }

    private fun setupWarningVisibility(editText: EditText, warningText: TextView) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                warningText.visibility = if (s.isNullOrBlank()) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}