package com.example.tripy.presentation.ui.main2

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tripy.R
import com.example.tripy.databinding.FragmentEditTripBinding
import com.example.tripy.databinding.ToolBarBinding
import com.example.tripy.presentation.ui.main.HomeActivity
import com.example.tripy.room.TripTable
import com.example.tripy.utils.AlertDialogHelper
import com.example.tripy.utils.Constants
import com.example.tripy.utils.showCurrencyDialog
import com.example.tripy.utils.showDatePicker
import com.example.tripy.viewmodels.TripViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTripFragment : Fragment() {

    private var _binding: FragmentEditTripBinding? = null
    private val binding get() = _binding!!

    private var _toolbarBinding: ToolBarBinding? = null
    private val toolbarBinding get() = _toolbarBinding!!

    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var tripname: String
    private lateinit var tripLocation: String
    private lateinit var currency: String
    private lateinit var trip: TripTable

    lateinit var backPressedCallback: OnBackPressedCallback

    private val viewModel by viewModels<TripViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditTripBinding.inflate(layoutInflater)
        _toolbarBinding = ToolBarBinding.bind(binding.toolbar.root)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip = arguments?.getParcelable(Constants.ARG_SELECTED_TRIP)!!

        startDate = trip.startDate
        endDate = trip.endDate
        currency = trip.currency

        setTripData(trip)

        toolbarBinding.title.text = getString(R.string.edit_trip)
        toolbarBinding.icon.setOnClickListener {
            showDiscard()
        }

        binding.saveTripBtn.setOnClickListener {
            tripname = binding.tripName.text.toString()
            tripLocation = binding.tripLocation.text.toString()
            Log.e("updateTrip name -->", "$tripname")
            trip.id.let { id ->
                updateTrip(id)
            }
        }
        binding.calendar.setOnClickListener {

            binding.calendar.isEnabled=false
            showDatePicker(
                requireActivity(),
                requireActivity().supportFragmentManager
            ) { start, end ->
                this.startDate = start
                this.endDate = end
                binding.selectedDate.text = "$start - $end"
                binding.selectedDate.visibility = View.VISIBLE

                binding.calendar.isEnabled=true
            }
        }

        binding.currency.setOnClickListener {
            showCurrencyDialog(requireActivity()) { selectedCurrency ->
                currency = selectedCurrency
                binding.selectedCurr.text = currency
            }
        }
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("callback-->","onbackpressed")
                showDiscard()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, // LifecycleOwner
            backPressedCallback
        )

    }

    private fun updateTrip(id: Int) {
        val trip = TripTable(
            id = id,
            tripName = tripname,
            tripLocation = tripLocation,
            startDate = startDate,
            endDate = endDate,
            currency = currency
        )
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        requireActivity().startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in,R.anim.slide_out)

        Log.e("trip", trip.toString())


        try {
            viewModel.updateTrip(
                id,
                tripname,
                tripLocation,
                startDate,
                endDate,
                currency
            )

        } catch (e: Exception) {
           // Toast.makeText(requireActivity(), "$e.message.toString()", Toast.LENGTH_SHORT)
        }


        Log.e("updateTrip-->", "trip data updated")

    }


    private fun setTripData(trip: TripTable) {
        val date = "${trip.startDate} - ${trip.endDate}"
        binding.tripName.text = trip.tripName.toEditable()
        binding.tripLocation.text = trip.tripLocation.toEditable()
        binding.selectedDate.visibility = View.VISIBLE
        binding.selectedCurr.visibility = View.VISIBLE
        binding.selectedCurr.text = trip.currency
        binding.selectedDate.text = date

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _toolbarBinding = null
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun showDiscard() {
        Log.d("editfragmen","in showdiscard fun")
        AlertDialogHelper.showAlertDialog(
            requireActivity(),
            "Discard Changes?",
            null,
            getString(R.string.discard),
            "",
            {


                //  parentFragmentManager.popBackStack()

//requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                requireActivity().finish()
                trip?.let {
                    setTripData(it)
                }
            }
        ) {}
    }




}
