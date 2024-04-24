package com.example.tripy.presentation.ui.main2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tripy.R
import com.example.tripy.databinding.FragmentTripDetailBinding
import com.example.tripy.presentation.Adapters.DaysAdapter
import com.example.tripy.presentation.Adapters.ExpenseAdapter
import com.example.tripy.room.ExpenseTable
import com.example.tripy.room.TripTable
import com.example.tripy.utils.AlertDialogHelper
import com.example.tripy.utils.Constants
import com.example.tripy.utils.DateUtils
import com.example.tripy.viewmodels.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TripDetailFragment : Fragment(),
    DaysAdapter.OnItemClickListener,
    ExpenseAdapter.OnEditButtonClickListener,
    ExpenseAdapter.OnDeleteButtonClickListener {

    private var _binding: FragmentTripDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var trip: TripTable
    private lateinit var selectedDate: String
    private var days: Int = 0

    private lateinit var  expenseAdapter:ExpenseAdapter
    private val viewModel by viewModels<ExpenseViewModel>()

    var expenses: List<ExpenseTable> = emptyList()

    private var totalExpense: Double? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTripDetailBinding.inflate(layoutInflater)

        val tripArgument: TripTable? = arguments?.getParcelable(Constants.ARG_SELECTED_TRIP)
        if (tripArgument != null) {
            trip = tripArgument
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        days = DateUtils.getDaysBetweenDates(trip.startDate, trip.endDate).toInt()

        val daysList = (1..days).toList()

        //by default date and day
        onItemClick(daysList[0])
//       expenses = listOf(
//           ExpenseTable(
//               id = 1,
//               tripId = 1,
//               description = "Sample expense",
//               amount = 100.0,
//               date = "2024-03-30",
//               category = Category(R.drawable.hotel,"abcd")
//           )
//       )

        binding.header.back.setOnClickListener {
            requireActivity().finish()
        }


        val calendarAdapter = DaysAdapter(daysList, this)
        expenseAdapter = ExpenseAdapter(
            this, this
        )



        // Observe expenses and update UI
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.currentExpense.collectLatest { expense ->
                expenses = expense
                Log.e("expense---> ...", expenses.size.toString())

                totalExpense = 0.0
                for (exp in expenses) {
                    totalExpense = totalExpense!! + exp.amount.toString().toDouble()
                }
                setHeader()
                // Update the expenses list and notify the adapter
              //  expenseAdapter.updateData(expenses)
                expenseAdapter.differ.submitList(expenses)
            }
        }


        binding.tripDetailRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = expenseAdapter
        }

        binding.tripCalendarRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = calendarAdapter
        }


    }

    override fun onResume() {
        super.onResume()
        (activity as TripActivity?)?.showBottomNavigationBar()

        expenseAdapter.differ.submitList(expenses)
     //   (activity as TripActivity?)?.hideBottomBarId()


    }

    private fun setHeader() {

        if (totalExpense != null) {
            Log.e("totalExpense-->", totalExpense.toString())
            binding.header.totalExpense.text = totalExpense.toString()
        }
        binding.header.expenseType.text = trip.currency
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onItemClick(day: Int) {

        val currentDate = trip.startDate
        val daysToAdd = day - 1

        selectedDate = currentDate

      viewLifecycleOwner.lifecycleScope.launch {

            val (nextDay, nextDate, date) = DateUtils.getNextDayAndDate(currentDate, daysToAdd)

            binding.header.date.text = nextDate.toString()
            binding.header.day.text = nextDay

            selectedDate = date

            viewModel.getExpenseByDate(selectedDate, trip.id)
        }


    }

    override fun onEditButtonClick(expense: ExpenseTable) {
        val fragment = ExpenseFragment()

        val bundle = Bundle().apply {
            putParcelable(Constants.ARG_SELECTED_TRIP, trip)
            putParcelable(Constants.ARG_SELECTED_EXPENSE, expense)
        }

        fragment.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }
    override fun onDeleteButtonClick(expense: ExpenseTable, itemView: View) {
        AlertDialogHelper.showAlertDialog(
            requireActivity(),
            "Delete Record?",
            R.drawable.delete,
            "Delete",
            "Cancel",
            {
                // Apply animation to the itemView
                val animation = AnimationUtils.loadAnimation(context, R.anim.slide_out)
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation) {
                        // Update total expense
                        totalExpense = totalExpense?.minus(expense.amount ?: 0.0)
                        binding.header.totalExpense.text = totalExpense.toString()



                        // Remove the expense from the list after animation ends
                        val position = expenseAdapter.differ.currentList.indexOf(expense)
                        if (position != -1) {
                            val newList = ArrayList(expenseAdapter.differ.currentList)
                            newList.removeAt(position)
                            expenseAdapter.differ.submitList(newList)
                        }

                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModel.deleteExpense(expenseTable = expense)
                        }

                    }

                    override fun onAnimationEnd(animation: Animation) {


                    }

                    override fun onAnimationRepeat(animation: Animation) {}
                })
                itemView.startAnimation(animation)
            }
        ) {}
    }




}


//        GlobalScope.launch(Dispatchers.Main) {
//
//            val (nextDay, nextDate) = getNextDayAndDate(currentDate, daysToAdd)
//
//
//            binding.header.date.text = nextDate.toString()
//            binding.header.day.text = nextDay.toString()
//        }